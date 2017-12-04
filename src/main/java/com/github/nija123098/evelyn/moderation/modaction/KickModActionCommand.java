package com.github.nija123098.evelyn.moderation.modaction;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.moderation.modaction.support.AbstractModAction;

import static com.github.nija123098.evelyn.moderation.modaction.support.AbstractModAction.ModActionLevel.KICK;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class KickModActionCommand extends AbstractCommand {
    public KickModActionCommand() {
        super(ModActionCommand.class, "kick", "kick", null, "k", "Kicks a user");
    }

    @Command
    public void command(Guild guild, User user, @Argument(info = "The user to be kicked") User target, @Argument(optional = true, info = "The reason") String reason) {
        guild.kickUser(target, reason);
        new AbstractModAction(guild, KICK, target, user, reason);
        new MessageMaker(target).append("You were kicked from " + guild.getName() + (reason == null || reason.isEmpty() ? "" : " for " + reason)).send();
    }

    @Override
    public boolean hasPermission(User user, Channel channel) {
        return !channel.isPrivate() && user.getPermissionsForGuild(channel.getGuild()).contains(DiscordPermission.KICK);
    }
}
