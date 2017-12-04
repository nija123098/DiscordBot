package com.github.nija123098.evelyn.helping.poll;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

import java.util.HashSet;
import java.util.Set;

import static com.github.nija123098.evelyn.config.ConfigCategory.STAT_TRACKING;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PollStorageConfig extends AbstractConfig<Set<Poll>, GlobalConfigurable> {
    public PollStorageConfig() {
        super("poll_storage", "", STAT_TRACKING, new HashSet<>(), "Storage for poll objects");
    }
}
