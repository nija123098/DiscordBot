package com.github.nija123098.evelyn.audio.configs.playlist;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PlaylistContentsConfig extends AbstractConfig<List<Track>, Playlist> {
    public PlaylistContentsConfig() {
        super("playlist_contents", "", ConfigCategory.STAT_TRACKING, new ArrayList<>(), "The contents of the playlist");
    }
}
