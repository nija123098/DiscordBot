package com.github.nija123098.evelyn.helping.poll;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * Made by nija123098 on 6/15/2017.
 */
public class PollIDCountConfig extends AbstractConfig<Integer, GlobalConfigurable> {
    public PollIDCountConfig() {// default to exceed max options to avoid accidental mix of option and poll ID
        super("current_money", "poll_id_count", ConfigCategory.STAT_TRACKING, 10, "The count of poll IDs to avoid duplication.");
    }
    static synchronized int getNewID(){
        return ConfigHandler.changeSetting(PollIDCountConfig.class, GlobalConfigurable.GLOBAL, integer -> integer + 1);
    }
}
