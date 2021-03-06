package com.github.nija123098.evelyn.audio.configs.playlist;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class UserPlaylistsConfig extends AbstractConfig<Set<String>, User> {
    public UserPlaylistsConfig() {
        super("user_playlists", "", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "The playlists a user has");
    }
}
