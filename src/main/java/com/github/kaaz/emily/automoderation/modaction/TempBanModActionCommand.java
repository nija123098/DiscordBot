package com.github.kaaz.emily.automoderation.modaction;

import com.github.kaaz.emily.automoderation.modaction.support.AbstractModAction;
import com.github.kaaz.emily.automoderation.modaction.support.TempBanActionConfig;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.Time;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class TempBanModActionCommand extends AbstractCommand {
    public TempBanModActionCommand() {
        super(ModActionCommand.class, "tempban", "tempban", null, "tb, t", "Temporarily bans a user, by default 1 day");
    }
    @Command
    public void command(Guild guild, User user, @Argument(info = "the user to be muted") User target, @Argument(info = "Case #") Integer cas, @Argument(optional = true, info = "duration of punishment") Time time, @Argument(info = "the reason", optional = true) String reason){
        long length = time != null ? time.timeUntil() : 3600000;
        ban(guild, target, length);
        new AbstractModAction(guild, AbstractModAction.ModActionLevel.TEMP_BAN, target, user, reason);
    }
    private static void ban(Guild guild, User user, long length){
        guild.banUser(user);
        ConfigHandler.changeSetting(TempBanActionConfig.class, GuildUser.getGuildUser(guild, user), map -> length + System.currentTimeMillis());
        ScheduleService.schedule(length, () -> unban(guild, user));
    }
    public static void unban(Guild guild, User user){
        guild.pardonUser(user.getID());
    }
}
