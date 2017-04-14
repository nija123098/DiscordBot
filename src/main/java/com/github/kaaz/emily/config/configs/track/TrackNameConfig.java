package com.github.kaaz.emily.config.configs.track;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class TrackNameConfig extends AbstractConfig<String, Track> {
    public TrackNameConfig() {
        super("track_name", BotRole.BOT_ADMIN, null, "The name of a track");
    }
}
