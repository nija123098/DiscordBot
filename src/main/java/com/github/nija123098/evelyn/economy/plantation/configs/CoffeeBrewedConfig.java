package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class CoffeeBrewedConfig extends AbstractConfig<String, User> {
    public CoffeeBrewedConfig() {
        super("coffee_brewed", "", ConfigCategory.STAT_TRACKING, "", "String Instant of when the coffee was brewed");
    }
}
