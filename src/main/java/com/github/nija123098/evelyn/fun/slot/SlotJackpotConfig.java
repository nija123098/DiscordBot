package com.github.nija123098.evelyn.fun.slot;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class SlotJackpotConfig extends AbstractConfig<Integer, Guild> {
    public SlotJackpotConfig() {
        super("guild_slot_jackpot", "Guild Jackpot", ConfigCategory.STAT_TRACKING, 12, "The slot jackpot for the guild");
    }
}