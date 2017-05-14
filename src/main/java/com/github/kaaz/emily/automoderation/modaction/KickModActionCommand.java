package com.github.kaaz.emily.automoderation.modaction;

import com.github.kaaz.emily.automoderation.modaction.support.AbstractModAction;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class KickModActionCommand extends AbstractCommand {
    public KickModActionCommand() {
        super(ModActionCommand.class, "kick", "kick", null, "k", "Kicks a user");
    }
    @Command
    public void command(Guild guild, User user, @Argument(info = "The user to be kicked") User target, @Argument(optional = true, info = "The reason") String reason){
        guild.kickUser(user);
        new AbstractModAction(guild, AbstractModAction.ModActionLevel.KICK, target, user, reason);
    }
}
