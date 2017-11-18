package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * Written by Soarnir 18/11/17
 */

public class EventNameConfig extends AbstractConfig<String, GlobalConfigurable> {
    public EventNameConfig() {
        super("event_name", ConfigCategory.STAT_TRACKING, "", "name of the event");
    }
}
