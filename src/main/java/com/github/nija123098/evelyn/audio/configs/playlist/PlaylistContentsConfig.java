package com.github.nija123098.evelyn.audio.configs.playlist;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.AbstractConfig;

import java.util.ArrayList;
import java.util.List;

import static com.github.nija123098.evelyn.config.ConfigCategory.STAT_TRACKING;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlaylistContentsConfig extends AbstractConfig<List<Track>, Playlist> {
    public PlaylistContentsConfig() {
        super("playlist_contents", "", STAT_TRACKING, new ArrayList<>(), "The contents of the playlist");
    }
}
