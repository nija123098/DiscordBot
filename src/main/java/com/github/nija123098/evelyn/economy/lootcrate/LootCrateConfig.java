package com.github.nija123098.evelyn.economy.lootcrate;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Written by Dxeo 21/11/2017
 */

public class LootCrateConfig extends AbstractConfig<Integer, User> {

    public LootCrateConfig() {
        super("loot_crates", "Loot Crates", ConfigCategory.STAT_TRACKING, 0, "EA");
        }

}