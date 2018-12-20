package com.github.nija123098.evelyn.moderation.gameroles;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.DiscordAdapter;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Presence;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordPresenceUpdate;
import com.github.nija123098.evelyn.launcher.Launcher;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GameRoleConfig extends AbstractConfig<Boolean, Role> {
    private Map<Guild, Set<Role>> roles = new ConcurrentHashMap<>();
    public GameRoleConfig() {
        super("game_role", "", ConfigCategory.GAME_TEMPORARY_CHANNELS, false, "Automatically assigns the role to users playing the Role's title game");
        Launcher.registerPostStartup(() -> ConfigHandler.getNonDefaultSettings(UserHasGameRoleConfig.class).keySet().forEach(user -> check(user, user.getPresence(), null)));
        Launcher.registerStartup(() -> this.getNonDefaultSettings().forEach((role, aBoolean) -> this.roles.computeIfAbsent(role.getGuild(), guild -> new HashSet<>()).add(role)));
        Launcher.registerStartup(() -> this.getNonDefaultSettings().keySet().stream().map(Role::getGuild).distinct().forEach(DiscordAdapter::managePresences));
    }// concurrency of roles makes the roles set concurrency safe
    @EventListener
    public void handle(DiscordPresenceUpdate update) {
        check(update.getUser(), update.getNewPresence(), update.getOldPresence());
    }
    public void check(User user, Presence presence, Presence oldPresence) {
        if (oldPresence != null && presence.getOptionalPlayingText().equals(oldPresence.getOptionalPlayingText())) return;
        // new MessageMaker(DiscordClient.getApplicationOwner()).appendRaw("STARTING").send();
        Set<Role> roles = new HashSet<>();
        user.getGuilds().forEach(guild -> {
            Set<Role> rol = this.roles.get(guild);
            if (rol != null) roles.addAll(rol);
        });
        if (roles.isEmpty()) return;
        if (oldPresence != null) oldPresence.getOptionalPlayingText().ifPresent(playText -> roles.forEach(role -> {
            if (role.getName().equals(playText)) {
                user.removeRole(role);
                ConfigHandler.setSetting(UserHasGameRoleConfig.class, user, false);
            }
        }));
        presence.getOptionalPlayingText().ifPresent(playText -> roles.forEach(role -> {
            if (role.getName().equals(playText)) {
                user.addRole(role);
                ConfigHandler.setSetting(UserHasGameRoleConfig.class, user, true);
            }
        }));
        // new MessageMaker(DiscordClient.getApplicationOwner()).appendRaw("WOOT").send();
    }
}
