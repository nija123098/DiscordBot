package com.github.nija123098.evelyn.moderation.gameroles;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Presence;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordPresenceUpdate;
import com.github.nija123098.evelyn.launcher.Launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GameRoleConfig extends AbstractConfig<Boolean, Role> {
    public GameRoleConfig() {
        super("game_role", "", ConfigCategory.GAME_TEMPORARY_CHANNELS, false, "Automatically assigns the role to users playing the Role's title game");
        Launcher.registerAsyncStartup(() -> ConfigHandler.getNonDefaultSettings(UserHasGameRoleConfig.class).keySet().forEach(user -> check(user, user.getPresence(), null)));
    }
    @EventListener
    public void handle(DiscordPresenceUpdate update){
        check(update.getUser(), update.getNewPresence(), update.getOldPresence());
    }
    public void check(User user, Presence presence, Presence oldPresence){
        if (oldPresence != null && presence.getOptionalPlayingText().equals(oldPresence.getOptionalPlayingText())) return;
        if (oldPresence != null) oldPresence.getOptionalStreamingUrl().ifPresent(playText -> rolesForGuilds(user.getGuilds()).forEach(role -> {
            if (role.getName().equals(playText)) {
                ConfigHandler.setSetting(UserHasGameRoleConfig.class, user, false);
                user.addRole(role);
            }
        }));
        presence.getOptionalPlayingText().ifPresent(playText -> rolesForGuilds(user.getGuilds()).forEach(role -> {
            if (role.getName().equals(playText)) {
                user.addRole(role);
                ConfigHandler.setSetting(UserHasGameRoleConfig.class, user, true);
            }
        }));
    }
    private List<Role> rolesForGuilds(Set<Guild> guilds) {
        List<Role> roles = new ArrayList<>(guilds.size());
        guilds.forEach(guild -> roles.addAll(guild.getRoles().stream().filter(this::getValue).collect(Collectors.toList())));
        return roles;
    }
}
