package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class LastCurrencyUseConfig extends AbstractConfig<String, User> {
    public LastCurrencyUseConfig() {
        super("last_currency_use", ConfigCategory.STAT_TRACKING, "", "The last time the bank command was used");
    }
}