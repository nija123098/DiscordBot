package com.github.kaaz.emily.automoderation.modaction;

import com.github.kaaz.emily.automoderation.modaction.support.AbstractModAction;
import com.github.kaaz.emily.automoderation.modaction.support.StrikeActionConfig;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class WarnModActionCommand extends AbstractCommand {
    public WarnModActionCommand() {
        super(ModActionCommand.class, "warn", "warn", null, "w", "Warns a user of their behavior and gives them a strike");
    }
    @Command
    public void command(@Argument User user, User invoker, Guild guild, String warning){
        new AbstractModAction(guild, AbstractModAction.ModActionLevel.WARN, user, invoker, warning);
        new MessageMaker(user).append("You have been warned in " + guild.getName() + " and gained 1 strike, totaling " + ConfigHandler.getSetting(StrikeActionConfig.class, GuildUser.getGuildUser(guild, user)) + "\n" + warning).send();
    }
}
