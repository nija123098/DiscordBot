package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class CurrentGrinderUpgradesConfig extends AbstractConfig<Integer, User> {
    public CurrentGrinderUpgradesConfig() {
        super("current_grinder_upgrades", "", ConfigCategory.STAT_TRACKING, 0, "number of grinder upgrades, limited by house level");
    }
}
