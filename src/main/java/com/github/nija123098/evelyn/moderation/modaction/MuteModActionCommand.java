package com.github.nija123098.evelyn.moderation.modaction;

import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.moderation.MuteRoleConfig;
import com.github.nija123098.evelyn.moderation.modaction.support.AbstractModAction;
import com.github.nija123098.evelyn.moderation.modaction.support.MuteActionConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.exeption.ConfigurationException;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.Time;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class MuteModActionCommand extends AbstractCommand {
    public MuteModActionCommand() {
        super(ModActionCommand.class, "mute", "mute", null, "m", "Mutes an annoying user for a specified amount of time or 1 hour by default");
    }
    @Command
    public void command(Guild guild, User user, @Argument(info = "the user to be muted") User target, @Argument(optional = true, replacement = ContextType.NONE, info = "duration of punishment") Time time, @Argument(info = "the reason", optional = true) String reason){
        if (ConfigHandler.getSetting(MuteRoleConfig.class, guild) == null) throw new ConfigurationException("No mute role is configured");
        long length = time != null ? time.timeUntil() : 3600000;
        mute(guild, target, length);
        new AbstractModAction(guild, AbstractModAction.ModActionLevel.MUTE, target, user, reason);
    }
    private static void mute(Guild guild, User user, long length){
        Set<Role> roles = new HashSet<>(user.getRolesForGuild(guild));
        long current = System.currentTimeMillis();
        roles.forEach(user::removeRole);
        user.addRole(ConfigHandler.getSetting(MuteRoleConfig.class, guild));
        ConfigHandler.changeSetting(MuteActionConfig.class, GuildUser.getGuildUser(guild, user), pair -> new Pair<>((pair == null ? 0 : pair.getKey() - current) + length + current, roles));
        ScheduleService.schedule(length, () -> {
            if (!(ConfigHandler.getSetting(MuteActionConfig.class, GuildUser.getGuildUser(guild, user)).getKey() + 10_000 > System.currentTimeMillis())) return;
            unmute(guild, user);
        });
    }
    public static void unmute(Guild guild, User user){
        ConfigHandler.alterSetting(MuteActionConfig.class, GuildUser.getGuildUser(guild, user), pair -> {
            pair.getValue().forEach(user::addRole);
            user.removeRole(ConfigHandler.getSetting(MuteRoleConfig.class, guild));
        });
    }
}
