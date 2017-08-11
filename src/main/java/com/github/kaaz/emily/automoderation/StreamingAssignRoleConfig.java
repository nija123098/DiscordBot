package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Presence;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordPresenceUpdate;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.launcher.Launcher;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.ThreadProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class StreamingAssignRoleConfig extends AbstractConfig<Role, Guild>{
    static {
        ThreadProvider.sub(() -> Launcher.registerStartup(() -> DiscordClient.getUsers().stream().filter(user -> !user.isBot()).forEach(user -> {
            if (user.getPresence().getStatus() == Presence.Status.STREAMING) {
                rolesForGuilds(user.getGuilds()).forEach(role -> {
                    try{user.addRole(role);
                    } catch (Exception ignored){}// more role order moved
                });
                ConfigHandler.setSetting(StreamingBeforeShutdownConfig.class, user, false);
            } else if (ConfigHandler.getSetting(StreamingBeforeShutdownConfig.class, user) && user.getPresence().getStatus() != Presence.Status.STREAMING) {
                rolesForGuilds(user.getGuilds()).forEach(role -> {
                    try{user.removeRole(role);
                    } catch (Exception ignored){}// more role order moved
                });
                ConfigHandler.setSetting(StreamingBeforeShutdownConfig.class, user, false);
            }
        })));
    }
    public StreamingAssignRoleConfig() {
        super("streaming_role", BotRole.GUILD_TRUSTEE, null, "The role to assign a streaming user");
    }
    @EventListener
    public void handle(DiscordPresenceUpdate event){
        if (event.getNewPresence().getStatus() == Presence.Status.STREAMING && event.getOldPresence().getStatus() == Presence.Status.STREAMING) return;
        if (event.getNewPresence().getStatus() == Presence.Status.STREAMING) {
            rolesForGuilds(event.getUser().getGuilds()).forEach(role -> {
                try{event.getUser().addRole(role);
                } catch (Exception ignored){}// todo role order moved
                ConfigHandler.setSetting(StreamingBeforeShutdownConfig.class, event.getUser(), false);
            });
        } else if (ConfigHandler.getSetting(StreamingBeforeShutdownConfig.class, event.getUser())){
            rolesForGuilds(event.getUser().getGuilds()).forEach(role -> {
                try{event.getUser().removeRole(role);
                } catch (Exception ignored){}// more role order moved here
            });
        }
    }
    private static Set<Role> rolesForGuilds(Set<Guild> guilds){
        Set<Role> roles = new HashSet<>(guilds.size() / 8);
        guilds.forEach(guild -> {
            Role role = ConfigHandler.getSetting(StreamingAssignRoleConfig.class, guild);
            if (role != null) roles.add(role);
        });
        return roles;
    }
    @Override
    protected void validateInput(Guild configurable, Role role) {
        if (role.getPosition() < DiscordClient.getOurUser().getRolesForGuild(configurable).get(0).getPosition()) throw new ArgumentException("For me to assign roles I must have a higher role");
    }
}
