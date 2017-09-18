package com.github.nija123098.evelyn.audio.configs.track;

import com.github.nija123098.evelyn.audio.DownloadableTrack;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 7/10/2017.
 */
public class TrackTypeConfig extends AbstractConfig<String, Track> {
    public TrackTypeConfig() {
        super("track_type", ConfigCategory.STAT_TRACKING, track -> track instanceof DownloadableTrack ? ((DownloadableTrack) track).getPreferredType() : null, "The track file type");
    }
}
