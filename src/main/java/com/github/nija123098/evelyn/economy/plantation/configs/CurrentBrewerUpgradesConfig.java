package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class CurrentBrewerUpgradesConfig extends AbstractConfig<Integer, User> {
    public CurrentBrewerUpgradesConfig() {
        super("current_brewer_upgrades", "", ConfigCategory.STAT_TRACKING, 0, "quality of the brew & number of upgrades");
    }
}
