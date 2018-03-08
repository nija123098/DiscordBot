package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.UserIssueException;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.ThreadHelper;
import com.github.nija123098.evelyn.util.Time;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DelayCommand extends AbstractCommand {
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Delay-Command-Thread"));
    public DelayCommand() {
        super("delay", ModuleLevel.NONE, null, null, "Executes a command after a specified delay, this command does not detect exceptions in advance and my soft fail");
    }
    @Command
    public void command(@Argument Time time, String s, User user, Message message, Reaction reaction){
        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> {
            try {
                CommandHandler.attemptInvocation(s, user, message, reaction);
            } catch (UserIssueException e) {
                e.makeMessage(user.getOrCreatePMChannel());
            } catch (Exception e) {
                Log.log("Exception running delayed command:" + s, e);
            }
        }, time.timeUntil(), TimeUnit.MILLISECONDS);
    }
}
