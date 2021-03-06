package com.github.nija123098.evelyn.economy.event.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class EventEndConfig extends AbstractConfig<Long, GlobalConfigurable> {
    public EventEndConfig() {
        super("event_end", "", ConfigCategory.STAT_TRACKING, 0L, "when the event will end");
    }
}
