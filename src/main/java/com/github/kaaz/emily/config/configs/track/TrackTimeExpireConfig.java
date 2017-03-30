package com.github.kaaz.emily.config.configs.track;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 3/28/2017.
 */
public class TrackTimeExpireConfig extends AbstractConfig<Long, Track>{
    public TrackTimeExpireConfig() {
        super("track_time_expire", BotRole.BOT_OWNER, 0L, "The last access time of the file, for deleting old tracks");
    }
}
