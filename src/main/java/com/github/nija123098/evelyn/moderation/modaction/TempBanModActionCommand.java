package com.github.nija123098.evelyn.moderation.modaction;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.moderation.modaction.support.AbstractModAction;
import com.github.nija123098.evelyn.moderation.modaction.support.TempBanActionConfig;
import com.github.nija123098.evelyn.util.Time;

import java.util.concurrent.TimeUnit;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TempBanModActionCommand extends AbstractCommand {
    public TempBanModActionCommand() {
        super(ModActionCommand.class, "tempban", "tempban", null, "tb, t", "Temporarily bans a user, by default 1 day");
    }

    @Command
    public void command(Guild guild, User user, @Argument(info = "the user to be muted") User target, @Argument(optional = true, info = "duration of punishment") Time time, @Argument(info = "the reason", optional = true) String reason) {
        long length = time != null ? time.timeUntil() : 3600000;
        ban(guild, target, length, reason);
        new AbstractModAction(guild, AbstractModAction.ModActionLevel.TEMP_BAN, target, user, reason);
        new MessageMaker(target).append("You were temporarily banned from " + guild.getName() + (reason == null || reason.isEmpty() ? "" : " for " + reason)).send();
    }

    @Override
    public boolean hasPermission(User user, Channel channel) {
        return !channel.isPrivate() && user.getPermissionsForGuild(channel.getGuild()).contains(DiscordPermission.BAN);
    }

    private static void ban(Guild guild, User user, long length, String reason) {
        guild.banUser(user, 7, reason);
        ConfigHandler.changeSetting(TempBanActionConfig.class, GuildUser.getGuildUser(guild, user), map -> length + System.currentTimeMillis());
        TempBanActionConfig.EXECUTOR_SERVICE.schedule(() -> unban(guild, user), length, TimeUnit.MILLISECONDS);
    }

    public static void unban(Guild guild, User user) {
        guild.pardonUser(user.getID());
        ConfigHandler.reset(TempBanActionConfig.class, GuildUser.getGuildUser(guild, user));
    }
}
