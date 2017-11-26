package com.github.nija123098.evelyn.fun.slot;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * Written by Dxeo 21/11/2017
 */

public class SlotJackpotConfig extends AbstractConfig<Integer, Guild> {

    public SlotJackpotConfig() {
        super("guild_slot_jackpot", "Guild Jackpot", ConfigCategory.STAT_TRACKING, 0, "maybe EA");

    }

    @Override
    public Integer setValue(Guild configurable, Integer value, boolean overrideCache) {
        return super.setValue(configurable, value, true);
    }
}