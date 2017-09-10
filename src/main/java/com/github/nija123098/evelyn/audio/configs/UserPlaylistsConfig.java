package com.github.nija123098.evelyn.audio.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

public class UserPlaylistsConfig extends AbstractConfig<Set<String>, User> {
    public UserPlaylistsConfig() {
        super("user_playlists", BotRole.SYSTEM, new HashSet<>(0), "The playlists a user has");
    }
}
