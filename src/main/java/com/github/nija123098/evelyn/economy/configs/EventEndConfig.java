package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * Written by Soarnir 18/11/17
 */

public class EventEndConfig extends AbstractConfig<String, GlobalConfigurable> {
    public EventEndConfig() {
        super("event_end", ConfigCategory.STAT_TRACKING, "", "when the event will end");
    }
}
