package com.github.kaaz.emily.audio.configs.track;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.config.AbstractCountingConfig;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/12/2017.
 */
public class PlayCountConfig extends AbstractCountingConfig<Track> {
    public PlayCountConfig() {
        super("play_count", BotRole.BOT_ADMIN, "The number of times a track has been played");
    }
}
