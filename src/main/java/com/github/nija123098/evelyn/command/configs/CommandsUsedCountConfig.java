package com.github.nija123098.evelyn.command.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CommandsUsedCountConfig extends AbstractConfig<Integer, User> {
    public CommandsUsedCountConfig() {
        super("commands_used_count", "", ConfigCategory.STAT_TRACKING, 0, "The number of commands used");
    }
    public static void increment(User user){
        ConfigHandler.changeSetting(CommandsUsedCountConfig.class, user, integer -> ++integer);
    }
}
