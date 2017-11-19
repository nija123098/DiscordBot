package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.Configurable;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class CurrentCurrencyConfig extends AbstractConfig<Integer, Configurable> {
    public CurrentCurrencyConfig() {
        super("current_currency", ConfigCategory.STAT_TRACKING, 0, "The amount of currency a config has");
    }
}
