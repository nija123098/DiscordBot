package com.github.nija123098.evelyn.config.configs.user;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class LastCommandTimeConfig extends AbstractConfig<Long, User> {
    private static final AtomicReference<LastCommandTimeConfig> CONFIG = new AtomicReference<>();
    public LastCommandTimeConfig() {
        super("last_command_time", "", ConfigCategory.STAT_TRACKING, -1L, "The last time the user used a command");
        CONFIG.set(this);
    }
    public static void update(User user) {
        CONFIG.get().setValue(user, System.currentTimeMillis());
    }

    public static long get(User user) {
        return CONFIG.get().getValue(user);
    }
}
