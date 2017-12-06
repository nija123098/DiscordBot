package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class PlantationUserLevelConfig extends AbstractConfig<Integer, User> {
    public PlantationUserLevelConfig() {
        super("plantation_skill_level", "", ConfigCategory.STAT_TRACKING, 0, "the current plantation skill level of the user");
    }
}
