package com.github.nija123098.evelyn.command.configs;

import com.github.nija123098.evelyn.config.AbstractCountingConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/21/2017.
 */
public class CommandsUsedCountConfig extends AbstractCountingConfig<User> {
    public CommandsUsedCountConfig() {
        super("commands_used_count", ConfigCategory.STAT_TRACKING, "The number of commands used");
    }
    public static void increment(User user){
        ConfigHandler.changeSetting(CommandsUsedCountConfig.class, user, integer -> ++integer);
    }
}
