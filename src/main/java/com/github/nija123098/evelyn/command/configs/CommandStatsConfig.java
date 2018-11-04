package com.github.nija123098.evelyn.command.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class CommandStatsConfig extends AbstractConfig<String, User> {
    public CommandStatsConfig() {
        super("command_stats", "", ConfigCategory.STAT_TRACKING, "0", "this is just a class to create the base stats table");
    }

}
