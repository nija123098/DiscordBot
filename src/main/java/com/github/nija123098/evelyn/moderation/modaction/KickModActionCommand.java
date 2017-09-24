package com.github.nija123098.evelyn.moderation.modaction;

import com.github.nija123098.evelyn.moderation.modaction.support.AbstractModAction;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class KickModActionCommand extends AbstractCommand {
    public KickModActionCommand() {
        super(ModActionCommand.class, "kick", "kick", null, "k", "Kicks a user");
    }
    @Command
    public void command(Guild guild, User user, @Argument(info = "The user to be kicked") User target, @Argument(optional = true, info = "The reason") String reason){
        guild.kickUser(target, reason);
        new AbstractModAction(guild, AbstractModAction.ModActionLevel.KICK, target, user, reason);
    }
    @Override
    public boolean hasPermission(User user, Channel channel) {
        return !channel.isPrivate() && user.getPermissionsForGuild(channel.getGuild()).contains(DiscordPermission.KICK);
    }
}
