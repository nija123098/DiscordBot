package com.github.nija123098.evelyn.audio.configs.track;

import com.github.nija123098.evelyn.audio.DownloadableTrack;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 7/10/2017.
 */
public class TrackTypeConfig extends AbstractConfig<String, Track> {
    public TrackTypeConfig() {
        super("track_type", BotRole.BOT_ADMIN, "The track file type", track -> track instanceof DownloadableTrack ? ((DownloadableTrack) track).getPreferredType() : null);
    }
}
