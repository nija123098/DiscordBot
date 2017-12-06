package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class CurrentColdBrewConfig extends AbstractConfig<Integer, User> {
    public CurrentColdBrewConfig() {
        super("current_cold_brew", "", ConfigCategory.STAT_TRACKING, 0, "How many cold brews are stored");
    }
}
