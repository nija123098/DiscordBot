package com.github.nija123098.evelyn.config.configs.user;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

public class LastCommandTimeConfig extends AbstractConfig<Long, User> {
    private static LastCommandTimeConfig config;
    public LastCommandTimeConfig() {
        super("last_command_time", BotRole.SYSTEM, -1L, "The last time the user used a command");
        config = this;
    }
    public static void update(User user){
        config.setValue(user, System.currentTimeMillis());
    }
}
