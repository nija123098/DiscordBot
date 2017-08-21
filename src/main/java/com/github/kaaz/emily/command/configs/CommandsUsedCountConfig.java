package com.github.kaaz.emily.command.configs;

import com.github.kaaz.emily.config.AbstractCountingConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/21/2017.
 */
public class CommandsUsedCountConfig extends AbstractCountingConfig<User> {
    public CommandsUsedCountConfig() {
        super("commands_used_count", BotRole.SYSTEM, "The number of commands used");
    }
    public static void increment(User user){
        ConfigHandler.changeSetting(CommandsUsedCountConfig.class, user, integer -> ++integer);
    }
}
