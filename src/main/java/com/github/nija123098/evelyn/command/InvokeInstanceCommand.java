package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.botconfiguration.configinterfaces.botconfig.BotSettings;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.CacheHelper;

/**
 * Invokes an instance by the {@link BotSettings#instanceId()}
 *
 * @author nija123098
 */
public class InvokeInstanceCommand extends AbstractCommand {
    public static final CacheHelper.ContainmentCache<Message> MESSAGES = new CacheHelper.ContainmentCache<>(240_000);

    public InvokeInstanceCommand() {
        super("invokeinstance", ModuleLevel.DEVELOPMENT, "ii", null, "Checks if this bot has the ID specified, otherwise doesn't respond to the command");
        this.setOkOnSuccess(false);
    }

    @Command
    public void command(@Argument Integer integer, String command, User user, Message message, @Context(softFail = true) Reaction reaction) {
        if (!integer.equals(ConfigProvider.BOT_SETTINGS.instanceId())) return;
        MESSAGES.add(message);
        CommandHandler.attemptInvocation("@Evelyn " + command, user, message, reaction);
    }
}
