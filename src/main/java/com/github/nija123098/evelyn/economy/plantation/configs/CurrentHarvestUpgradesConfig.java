package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class CurrentHarvestUpgradesConfig extends AbstractConfig<Integer, User> {
    public CurrentHarvestUpgradesConfig() {
        super("current_harvest_upgrades", "", ConfigCategory.STAT_TRACKING, 0, "current coffee plants level, not limited by house level");
    }
}
