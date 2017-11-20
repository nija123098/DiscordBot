package com.github.nija123098.evelyn.helping.poll;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 6/16/2017.
 */
public class PollStorageConfig extends AbstractConfig<Set<Poll>, GlobalConfigurable> {
    public PollStorageConfig() {
        super("poll_storage", "", ConfigCategory.STAT_TRACKING, new HashSet<>(), "Storage for poll objects");
    }
}
