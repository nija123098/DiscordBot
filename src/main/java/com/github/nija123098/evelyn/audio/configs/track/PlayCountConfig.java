package com.github.nija123098.evelyn.audio.configs.track;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.AbstractCountingConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 6/12/2017.
 */
public class PlayCountConfig extends AbstractCountingConfig<Track> {
    public PlayCountConfig() {
        super("play_count", ConfigCategory.STAT_TRACKING, "The number of times a track has been played");
    }
}
