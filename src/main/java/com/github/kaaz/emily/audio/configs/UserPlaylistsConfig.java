package com.github.kaaz.emily.audio.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

public class UserPlaylistsConfig extends AbstractConfig<Set<String>, User> {
    public UserPlaylistsConfig() {
        super("user_playlists", BotRole.SYSTEM, new HashSet<>(0), "The playlists a user has");
    }
}
