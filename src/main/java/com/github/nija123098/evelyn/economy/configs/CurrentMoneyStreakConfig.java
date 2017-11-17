package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.Configurable;

/**
 * Made by Celestialdeath99 on 11/16/2017.
 */

public class CurrentMoneyStreakConfig extends AbstractConfig<Integer, Configurable> {
    public CurrentMoneyStreakConfig() {
        super("current_streak", ConfigCategory.STAT_TRACKING, 0, "The number of times a user has used the claim command in a row");
    }
}
