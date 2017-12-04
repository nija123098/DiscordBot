package com.github.nija123098.evelyn.audio.configs.track;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TrackTimeExpireConfig extends AbstractConfig<Long, Track> {
    public TrackTimeExpireConfig() {
        super("track_time_expire", "", ConfigCategory.STAT_TRACKING, 0L, "The last access time of the file, for deleting old tracks");
    }
}
