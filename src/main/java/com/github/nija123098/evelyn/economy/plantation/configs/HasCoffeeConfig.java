package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class HasCoffeeConfig extends AbstractConfig<Boolean, User> {
    public HasCoffeeConfig() {
        super("has_coffee", "", ConfigCategory.STAT_TRACKING, false, "whether the coffee is real or not");
    }
}
