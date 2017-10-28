package com.github.nija123098.evelyn.moderation.modaction;

import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
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
public class BanModActionCommand extends AbstractCommand {
    public BanModActionCommand() {
        super(ModActionCommand.class, "ban", "ban", null, "b", "Bans a user");
    }
    @Command
    public void command(Guild guild, User user, @Argument(info = "The user to be kicked") User target, @Argument(optional = true, info = "The reason") String reason){
        guild.banUser(target, 7, reason);
        new AbstractModAction(guild, AbstractModAction.ModActionLevel.BAN, target, user, reason);
        new MessageMaker(target).append("You were temporarily banned from " + guild.getName() + (reason == null || reason.isEmpty() ? "" :  " for " + reason)).send();
    }
    @Override
    public boolean hasPermission(User user, Channel channel) {
        return !channel.isPrivate() && user.getPermissionsForGuild(channel.getGuild()).contains(DiscordPermission.BAN);
    }
}
