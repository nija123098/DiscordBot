package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * Written by Soarnir 18/11/17
 */

public class EventActiveConfig extends AbstractConfig<Boolean, GlobalConfigurable> {
    public EventActiveConfig() {
        super("event_active", "", ConfigCategory.STAT_TRACKING, false, "whether a special event is active");
    }
}
