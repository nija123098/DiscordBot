package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.economy.ItemComponent;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 5/16/2017.
 */
public class CurrentComponentsConfig extends AbstractConfig<Map<ItemComponent, Integer>, Configurable> {
    public CurrentComponentsConfig() {
        super("current_components", BotRole.BOT_ADMIN, new HashMap<>(), "The current components a thing has");
    }
}
