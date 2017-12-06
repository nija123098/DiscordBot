package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class CurrentRoastedBeansConfig extends AbstractConfig<Integer, User> {
    public CurrentRoastedBeansConfig () {
        super("current_roasted_beans", "", ConfigCategory.STAT_TRACKING, 0, "how many roasted beans a user has");
    }
}
