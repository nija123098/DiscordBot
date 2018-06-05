package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class CoffeeBrewedConfig extends AbstractConfig<Long, User> {
    public CoffeeBrewedConfig() {// 0 represents being a poor soul without coffee
        super("coffee_brewed", "", ConfigCategory.STAT_TRACKING, 0L, "Epoch of when the coffee was brewed");
    }
}
