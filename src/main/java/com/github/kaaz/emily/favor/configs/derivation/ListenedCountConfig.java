package com.github.kaaz.emily.favor.configs.derivation;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 8/10/2017.
 */
public class ListenedCountConfig extends AbstractConfig<Integer, Track> {
    public ListenedCountConfig() {
        super("listened_count", BotRole.SYSTEM, 0, "The number of times a user has listened entirely to this track");
    }
}
