package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Written by Soarnir 25/9/17
 */

public class CurrentMoneyStreakConfig extends AbstractConfig<Integer, User> {
    public CurrentMoneyStreakConfig() {
        super("current_streak", ConfigCategory.STAT_TRACKING, 0, "streak in days where user has used claim command");
    }
}
