package com.github.nija123098.evelyn.moderation.modaction;

import com.github.nija123098.evelyn.moderation.modaction.support.AbstractModAction;
import com.github.nija123098.evelyn.moderation.modaction.support.StrikeActionConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class WarnModActionCommand extends AbstractCommand {
    public WarnModActionCommand() {
        super(ModActionCommand.class, "warn", "warn", null, "w", "Warns a user of their behavior and gives them a strike");
    }
    @Command
    public void command(@Argument User user, User invoker, Guild guild, @Argument(info = "the reason",optional = true) String warning){
        new AbstractModAction(guild, AbstractModAction.ModActionLevel.WARN, user, invoker, warning);
        new MessageMaker(user).append("You have been warned in " + guild.getName() + " and gained 1 strike, totaling " + ConfigHandler.getSetting(StrikeActionConfig.class, GuildUser.getGuildUser(guild, user)) + "\n" + warning).send();
    }
}
