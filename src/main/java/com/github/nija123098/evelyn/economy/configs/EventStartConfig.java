package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * Written by Soarnir 18/11/17
 */

public class EventStartConfig extends AbstractConfig<String, GlobalConfigurable> {
    public EventStartConfig() {
        super("event_start", "", ConfigCategory.STAT_TRACKING, "", "when the next event will start");
    }
}
