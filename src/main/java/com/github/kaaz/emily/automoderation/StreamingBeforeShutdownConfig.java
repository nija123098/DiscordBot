package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/22/2017.
 */
public class StreamingBeforeShutdownConfig extends AbstractConfig<Boolean, User> {
    public StreamingBeforeShutdownConfig() {
        super("streaming_after_shutdown", BotRole.SYSTEM, false, "For optimization");
    }
}
