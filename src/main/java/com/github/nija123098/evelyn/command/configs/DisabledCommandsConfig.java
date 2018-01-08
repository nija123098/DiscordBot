package com.github.nija123098.evelyn.command.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class DisabledCommandsConfig extends AbstractConfig<Set<String>, GlobalConfigurable> {
    public DisabledCommandsConfig() {
        super("current_money", "disabled_commands", ConfigCategory.STAT_TRACKING, new HashSet<>(), "The globally disabled commands.");
    }
}
