package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class LastCoffeeHarvestConfig extends AbstractConfig<String, User> {
    public LastCoffeeHarvestConfig() {
        super("last_coffee_harvest", "", ConfigCategory.STAT_TRACKING, "2017-10-10T12:00:00Z", "Instant String of when the users beans were collected");
    }
}
