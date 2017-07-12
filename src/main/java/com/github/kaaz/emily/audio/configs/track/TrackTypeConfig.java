package com.github.kaaz.emily.audio.configs.track;

import com.github.kaaz.emily.audio.DownloadableTrack;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 7/10/2017.
 */
public class TrackTypeConfig extends AbstractConfig<String, Track> {
    public TrackTypeConfig() {
        super("track_type", BotRole.BOT_ADMIN, "The track file type", track -> track instanceof DownloadableTrack ? ((DownloadableTrack) track).getPreferredType() : null);
    }
}
