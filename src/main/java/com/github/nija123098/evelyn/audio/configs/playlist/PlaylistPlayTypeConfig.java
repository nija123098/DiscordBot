package com.github.nija123098.evelyn.audio.configs.playlist;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class PlaylistPlayTypeConfig extends AbstractConfig<Playlist.PlayType, Playlist> {
    public PlaylistPlayTypeConfig() {
        super("playlist_play_type", ConfigCategory.STAT_TRACKING, Playlist.PlayType.RANDOM,
                "The play type for the playlist to decide the order of the songs played");
    }
}
