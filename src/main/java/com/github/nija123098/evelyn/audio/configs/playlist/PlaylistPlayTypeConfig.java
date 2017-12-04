package com.github.nija123098.evelyn.audio.configs.playlist;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.config.AbstractConfig;

import static com.github.nija123098.evelyn.audio.Playlist.PlayType;
import static com.github.nija123098.evelyn.audio.Playlist.PlayType.RANDOM;
import static com.github.nija123098.evelyn.config.ConfigCategory.STAT_TRACKING;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlaylistPlayTypeConfig extends AbstractConfig<PlayType, Playlist> {
    public PlaylistPlayTypeConfig() {
        super("playlist_play_type", "", STAT_TRACKING, RANDOM,
                "The play type for the playlist to decide the order of the songs played");
    }
}
