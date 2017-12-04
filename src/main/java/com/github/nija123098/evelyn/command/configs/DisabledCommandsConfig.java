package com.github.nija123098.evelyn.command.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DisabledCommandsConfig extends AbstractConfig<Set<String>, GlobalConfigurable> {
    public DisabledCommandsConfig() {
        super("disabled_commands", "", ConfigCategory.STAT_TRACKING, new HashSet<>(), "The globally disabled commands.");
    }
}
