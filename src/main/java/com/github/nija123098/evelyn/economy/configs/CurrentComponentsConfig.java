package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.currencytransfer.ItemComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrentComponentsConfig extends AbstractConfig<Map<ItemComponent, Integer>, User> {
    public CurrentComponentsConfig() {
        super("current_components", "", ConfigCategory.STAT_TRACKING, new HashMap<>(), "The current components a user has");
    }
}
