package com.github.nija123098.evelyn.audio.configs.track;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 3/28/2017.
 */
public class TrackTimeExpireConfig extends AbstractConfig<Long, Track> {
    public TrackTimeExpireConfig() {
        super("track_time_expire", BotRole.BOT_OWNER, 0L, "The last access time of the file, for deleting old tracks");
    }
}
