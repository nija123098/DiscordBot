package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * Written by Soarnir 18/11/17
 */

public class EventBonusConfig extends AbstractConfig<Integer, GlobalConfigurable> {
    public EventBonusConfig() {
        super("event_bonus", "", ConfigCategory.STAT_TRACKING, 0, "base event bonus");
    }
}
