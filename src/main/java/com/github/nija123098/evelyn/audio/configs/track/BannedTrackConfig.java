package com.github.nija123098.evelyn.audio.configs.track;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 7/4/2017.
 */
public class BannedTrackConfig extends AbstractConfig<Boolean, Track> {
    private static BannedTrackConfig CONFIG;
    public BannedTrackConfig() {
        super("banned_track", ConfigCategory.STAT_TRACKING, false, "If the track is banned from being played on the global playlist");
        CONFIG = this;
    }
    public static void ban(Track track){
        CONFIG.setValue(track, true);
    }
}
