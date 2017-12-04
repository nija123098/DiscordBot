package com.github.nija123098.evelyn.economy.event.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class EventActiveConfig extends AbstractConfig<Boolean, GlobalConfigurable> {
    public EventActiveConfig() {
        super("event_active", "", ConfigCategory.STAT_TRACKING, false, "whether a special event is active");
    }
}
