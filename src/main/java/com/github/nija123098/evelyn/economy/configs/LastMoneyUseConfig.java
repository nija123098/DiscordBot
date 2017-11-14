package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.time.Instant;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class LastMoneyUseConfig extends AbstractConfig<String, User> {
    public LastMoneyUseConfig() {
        super("last_money_use", ConfigCategory.STAT_TRACKING, Instant.parse("2017-11-10T00:00:00Z").toString(), "The last time the claim command was used");
    }
}