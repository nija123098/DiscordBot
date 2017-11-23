package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Written by Soarnir 19/9/17
 */

public class CurrentBeansConfig extends AbstractConfig<Integer, User> {
    public CurrentBeansConfig () {
        super("current_beans", "", ConfigCategory.STAT_TRACKING, 0, "how many beans a user has");
    }
    public boolean checkDefault(){
        return false;
    }
}
