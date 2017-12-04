package com.github.nija123098.evelyn.moderation.modaction;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.moderation.modaction.support.AbstractModAction;
import com.github.nija123098.evelyn.moderation.modaction.support.WarningLogConfig;

import static com.github.nija123098.evelyn.config.ConfigHandler.alterSetting;
import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.moderation.modaction.support.AbstractModAction.ModActionLevel.WARN;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class WarnModActionCommand extends AbstractCommand {
    public WarnModActionCommand() {
        super(ModActionCommand.class, "warn", "warn", null, "w", "Warns a user of their behavior and gives them a strike");
    }

    @Command
    public void command(@Argument User user, User invoker, Guild guild, GuildUser guildUser, @Argument(info = "the reason", optional = true) String warning) {
        new AbstractModAction(guild, WARN, user, invoker, warning);
        new MessageMaker(user).append("You have been warned in " + guild.getName() + " and gained 1 strike, totaling " + getSetting(WarningLogConfig.class, guildUser).size() + "\n" + warning).send();
        alterSetting(WarningLogConfig.class, guildUser, list -> list.add(warning != null && warning.isEmpty() ? "no reason given" : warning));
    }
}
