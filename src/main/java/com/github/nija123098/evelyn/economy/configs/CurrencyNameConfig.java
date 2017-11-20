package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class CurrencyNameConfig extends AbstractConfig<String, Guild> {
    public CurrencyNameConfig() {
        super("currency_name", "", ConfigCategory.STAT_TRACKING, "cookies", "The name of the guild currency");
    }
}
