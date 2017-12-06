package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class CurrentSteepedCoffeeBatchConfig extends AbstractConfig<Integer, User> {
    public CurrentSteepedCoffeeBatchConfig() {
        super("current_steeped_batch", "", ConfigCategory.STAT_TRACKING, 0, "number of cold brews in current steeped coffee batch");
    }
}
