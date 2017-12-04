package com.github.nija123098.evelyn.audio.configs.track;

import com.github.nija123098.evelyn.audio.DownloadableTrack;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;

/**
 * @author nija123098
 * @since 1.0.0
 */

public class TrackTypeConfig extends AbstractConfig<String, Track> {
    public TrackTypeConfig() {
        super("track_type", "", ConfigCategory.STAT_TRACKING, track -> track instanceof DownloadableTrack ? ((DownloadableTrack) track).getPreferredType() : null, "The track file type");
    }
}
