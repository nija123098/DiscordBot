package com.github.kaaz.emily.config.configs.playlist;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Playlist;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class PlaylistPlayTypeConfig extends AbstractConfig<Playlist.PlayType, Playlist> {
    public PlaylistPlayTypeConfig() {
        super("playlist_play_type", BotRole.USER, Playlist.PlayType.RANDOM,
                "The play type for the playlist to decide the order of the songs played");
    }
}
