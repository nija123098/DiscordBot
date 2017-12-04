package com.github.nija123098.evelyn.helping.poll;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

import static com.github.nija123098.evelyn.config.ConfigCategory.STAT_TRACKING;
import static com.github.nija123098.evelyn.config.GlobalConfigurable.GLOBAL;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PollIDCountConfig extends AbstractConfig<Integer, GlobalConfigurable> {
    public PollIDCountConfig() {// default to exceed max options to avoid accidental mix of option and poll ID
        super("poll_id_count", "", STAT_TRACKING, 10, "The count of poll IDs to avoid duplication.");
    }

    static synchronized int getNewID() {
        return ConfigHandler.changeSetting(PollIDCountConfig.class, GLOBAL, integer -> integer + 1);
    }
}
