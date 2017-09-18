package com.github.nija123098.evelyn.config.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.perms.BotRole;

public class ConfigurableExistsConfig extends AbstractConfig<Boolean, Configurable> {
    public ConfigurableExistsConfig() {
        super("object_exists", ConfigCategory.STAT_TRACKING, false, "Indicates for getting all entities if a configurable exists");
    }
}
