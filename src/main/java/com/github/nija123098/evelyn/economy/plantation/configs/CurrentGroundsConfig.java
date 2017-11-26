package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Written by Soarnir 21/9/17
 */

public class CurrentGroundsConfig extends AbstractConfig<Integer, User> {
    public CurrentGroundsConfig () {
        super("current_grounds", "", ConfigCategory.STAT_TRACKING, 0, "how many grounds a user has");
    }
}
