package com.github.kaaz.emily.automoderation.modaction;

import com.github.kaaz.emily.automoderation.modaction.support.AbstractModAction;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class BanModActionCommand extends AbstractCommand {
    public BanModActionCommand() {
        super(ModActionCommand.class, "ban", "ban", null, "b", "Bans a user");
    }
    @Command
    public void command(Guild guild, User user, @Argument(info = "The user to be kicked") User target, @Argument(optional = true, info = "The reason") String reason){
        guild.banUser(user);
        new AbstractModAction(guild, AbstractModAction.ModActionLevel.BAN, target, user, reason);
    }
}
