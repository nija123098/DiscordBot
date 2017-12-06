package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class PlantationUserXPConfig extends AbstractConfig<Integer, User> {
    public PlantationUserXPConfig() {
        super("plantation_xp", "", ConfigCategory.STAT_TRACKING, 0, "the current amount of user experience");
    }
}
