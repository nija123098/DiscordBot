package com.github.nija123098.evelyn.economy.event.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class EventStartConfig extends AbstractConfig<String, GlobalConfigurable> {
    public EventStartConfig() {
        super("event_start", "", ConfigCategory.STAT_TRACKING, "", "when the next event will start");
    }
}
