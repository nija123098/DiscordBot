package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class CurrentHouseUpgradesConfig extends AbstractConfig<Integer, User> {
    public CurrentHouseUpgradesConfig() {
        super("current_house_upgrades", "", ConfigCategory.STAT_TRACKING, 0, "current house level, sets the max level of facilities");
    }
}
