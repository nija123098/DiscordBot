package com.github.kaaz.emily.helping.poll;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 6/16/2017.
 */
public class PollStorageConfig extends AbstractConfig<Set<Poll>, GlobalConfigurable> {
    public PollStorageConfig() {
        super("poll_storage", BotRole.SYSTEM, new HashSet<>(), "Storage for poll objects");
    }
}
