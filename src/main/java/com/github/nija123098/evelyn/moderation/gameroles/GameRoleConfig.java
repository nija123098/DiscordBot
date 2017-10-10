package com.github.nija123098.evelyn.moderation.gameroles;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Presence;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordPresenceUpdate;
import com.github.nija123098.evelyn.launcher.BotConfig;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.util.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoleConfig extends AbstractConfig<Boolean, Role> {
    private static final Map<User, Role> PREVIOUS = new ConcurrentHashMap<>();
    public GameRoleConfig() {
        super("game_role", ConfigCategory.GAME_TEMPORARY_CHANNELS, false, "Automatically assigns the role to users playing the Role's title game");
        Launcher.registerAsyncStartup(() -> DiscordClient.getUsers().forEach(user -> check(user, user.getPresence())));
    }
    @EventListener
    public void handle(DiscordPresenceUpdate update){
        check(update.getUser(), update.getNewPresence());
    }
    public void check(User user, Presence presence){
        Role previous = PREVIOUS.get(user);
        if (presence.getOptionalPlayingText().isPresent()) user.getGuilds().forEach(guild -> {
                Role role = guild.getRoles().stream().filter(this::getValue).filter(r -> r.getName().equals(presence.getPlayingText())).findFirst().orElse(null);
                if (Objects.equals(role, previous)) return;
                if (previous != null) {
                    user.removeRole(previous);
                    PREVIOUS.remove(user);
                }
                if (role != null){
                    user.addRole(role);
                    PREVIOUS.put(user, role);
                }
            });
        else if (previous != null) user.removeRole(previous);
    }
}
