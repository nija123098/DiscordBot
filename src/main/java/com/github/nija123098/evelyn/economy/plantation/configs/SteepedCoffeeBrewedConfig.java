package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class SteepedCoffeeBrewedConfig extends AbstractConfig<String, User> {
    public SteepedCoffeeBrewedConfig() {
        super("steeped_coffee_brewed", "", ConfigCategory.STAT_TRACKING, "", "when the batch of cold brew started");
    }
}
