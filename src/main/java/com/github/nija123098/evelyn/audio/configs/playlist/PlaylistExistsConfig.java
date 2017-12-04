package com.github.nija123098.evelyn.audio.configs.playlist;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.config.AbstractConfig;

import static com.github.nija123098.evelyn.config.ConfigCategory.STAT_TRACKING;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlaylistExistsConfig extends AbstractConfig<Boolean, Playlist> {
    public PlaylistExistsConfig() {
        super("playlist_exists", "", STAT_TRACKING, false, "If a playlist exists");
    }
}
