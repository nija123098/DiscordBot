package com.github.kaaz.emily.audio.configs.track;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/24/2017.
 */
public class DurationTimeConfig extends AbstractConfig<Long, Track> {
    public DurationTimeConfig() {
        super("track_play_time", BotRole.SYSTEM, null, "The time it takes to play the track");
    }
}
