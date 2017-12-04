package com.github.nija123098.evelyn.audio.configs.track;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlayCountConfig extends AbstractConfig<Integer, Track> {
    public PlayCountConfig() {
        super("play_count", "", ConfigCategory.STAT_TRACKING, 0, "The number of times a track has been played");
    }
}
