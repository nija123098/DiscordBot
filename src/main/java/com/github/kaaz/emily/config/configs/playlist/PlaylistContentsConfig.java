package com.github.kaaz.emily.config.configs.playlist;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Playlist;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PlaylistContentsConfig extends AbstractConfig<Set<String>, Playlist> {
    public PlaylistContentsConfig() {
        super("playlist_contents", BotRole.BOT_ADMIN, new HashSet<>(), "The contents of the playlist");
    }
}
