package com.github.nija123098.evelyn.information.rss;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Made by nija123098 on 6/18/2017.
 */
public class RSSLastCheckConfig extends AbstractConfig<Long, GlobalConfigurable> {
    public RSSLastCheckConfig() {
        super("rss_last_check", ConfigCategory.STAT_TRACKING, 0L, "The last rss check in millis");
    }
    public static long getAndUpdate(){
        AtomicLong val = new AtomicLong();
        ConfigHandler.changeSetting(RSSLastCheckConfig.class, GlobalConfigurable.GLOBAL, aLong -> {
            val.set(aLong);
            return System.currentTimeMillis();
        });
        return val.get();
    }
}
