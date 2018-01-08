package com.github.nija123098.evelyn.audio.configs.track;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;

/**
 * Made by nija123098 on 6/12/2017.
 */
public class PlayCountConfig extends AbstractConfig<Integer, Track> {
    public PlayCountConfig() {
        super("play_count", ConfigCategory.STAT_TRACKING, 0, "The number of times a track has been played");
    }
}
