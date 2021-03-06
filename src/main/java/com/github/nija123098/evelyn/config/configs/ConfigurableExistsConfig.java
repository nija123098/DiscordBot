package com.github.nija123098.evelyn.config.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.Configurable;

/**
 * A config which assists in keeping track if an
 * object of any type exists or used to exist.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class ConfigurableExistsConfig extends AbstractConfig<Boolean, Configurable> {
    public ConfigurableExistsConfig() {
        super("object_exists", "", ConfigCategory.STAT_TRACKING, false, "Indicates for getting all entities if a configurable exists");
    }
}
