package com.github.nija123098.evelyn.audio.configs.track;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;

/**
 * Tracks if a track is banned from the global playlist.
 * Such tracks will not be considered in global playlist
 * nor be considered for favor balancing in the global playlist.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class BannedTrackConfig extends AbstractConfig<Boolean, Track> {
    private static BannedTrackConfig CONFIG;
    public BannedTrackConfig() {
        super("banned_track", "", ConfigCategory.STAT_TRACKING, false, "If the track is banned from being played on the global playlist");
        CONFIG = this;
    }
    public static void ban(Track track){
        CONFIG.setValue(track, true);
    }
}
