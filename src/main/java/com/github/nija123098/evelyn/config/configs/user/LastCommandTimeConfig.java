package com.github.nija123098.evelyn.config.configs.user;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

public class LastCommandTimeConfig extends AbstractConfig<Long, User> {
    private static LastCommandTimeConfig config;
    public LastCommandTimeConfig() {
        super("current_money", "last_command_time", ConfigCategory.STAT_TRACKING, -1L, "The last time the user used a command");
        config = this;
    }
    public static void update(User user){
        config.setValue(user, System.currentTimeMillis());
    }
}
