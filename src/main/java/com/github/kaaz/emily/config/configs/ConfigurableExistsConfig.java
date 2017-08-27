package com.github.kaaz.emily.config.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.perms.BotRole;

public class ConfigurableExistsConfig extends AbstractConfig<Boolean, Configurable> {
    public ConfigurableExistsConfig() {
        super("object_exists", BotRole.SYSTEM, false, "Indicates for getting all entities if a configurable exists");
    }
}
