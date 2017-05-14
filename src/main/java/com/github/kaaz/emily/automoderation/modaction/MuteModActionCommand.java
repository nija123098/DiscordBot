package com.github.kaaz.emily.automoderation.modaction;

import com.github.kaaz.emily.automoderation.MuteRoleConfig;
import com.github.kaaz.emily.automoderation.modaction.support.AbstractModAction;
import com.github.kaaz.emily.automoderation.modaction.support.MuteActionConfig;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.exeption.ConfigurationException;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.Time;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class MuteModActionCommand extends AbstractCommand {
    public MuteModActionCommand() {
        super(ModActionCommand.class, "mute", "mute", null, "m", "Mutes an annoying user for a specified amount of time or 1 hour");
    }
    @Command
    public void command(Guild guild, User user, @Argument(info = "the user to be muted") User target, @Argument(info = "Case #") Integer cas, @Argument(optional = true, info = "duration of punishment") Time time, @Argument(info = "the reason", optional = true) String reason){
        long length = time != null ? time.timeUntil() : 3600000;
        mute(guild, target, length);
        new AbstractModAction(guild, AbstractModAction.ModActionLevel.MUTE, target, user, reason);
    }
    @Override
    protected void checkSetupRequirements(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args){
        if (ConfigHandler.getSetting(MuteRoleConfig.class, guild) == null) throw new ConfigurationException("No mute role is configured");
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
