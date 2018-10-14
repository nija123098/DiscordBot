package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.UserIssueException;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class DelayCommandConfigs extends AbstractConfig<List<DelayCommandConfigs.DelayedCommand>, GlobalConfigurable> {
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "Delay-Command-Config-Executor"));
    private static AtomicReference<DelayCommandConfigs> reference = new AtomicReference<>();
    public DelayCommandConfigs() {
        super("delayed_commands", "Delayed Commands", ConfigCategory.STAT_TRACKING, globalConfigurable -> new ArrayList<>(0), "A list of commands to be run");
        reference.set(this);
        this.getValue(GlobalConfigurable.GLOBAL).forEach(DelayCommandConfigs::command);
    }

    public static void command(DelayedCommand command) {// , Reaction reaction
        if (command.time.schedualed() + 1_000 <= System.currentTimeMillis()) command.execute();
        else SCHEDULED_EXECUTOR_SERVICE.schedule(command::execute, command.time.timeUntil(), TimeUnit.MILLISECONDS);
    }

    public static class DelayedCommand {
        private Time time;
        private String command;
        private String user;
        private String message;

        public DelayedCommand(Time time, String command, String user, String message) {
            this.time = time;
            this.command = command;
            this.user = user;
            this.message = message;
        }

        public void execute() {
            User user = User.getUser(this.user);
            if (user == null) {
                reference.get().alterSetting(GlobalConfigurable.GLOBAL, delayedCommands -> delayedCommands.remove(this));
                return;
            }
            Message message = Message.getMessage(this.message);
            if (message == null) {
                reference.get().alterSetting(GlobalConfigurable.GLOBAL, delayedCommands -> delayedCommands.remove(this));
                return;
            }
            try {
                CommandHandler.attemptInvocation(DiscordClient.getOurUser().mention() + " " + this.command, user, message, null);
            } catch (UserIssueException e) {
                e.makeMessage(user.getOrCreatePMChannel());
            } catch (Exception e) {
                Log.log("Exception running delayed command: " + this.command, e);
            }
        }
    }

}
