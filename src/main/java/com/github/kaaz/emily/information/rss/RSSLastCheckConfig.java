package com.github.kaaz.emily.information.rss;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.perms.BotRole;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Made by nija123098 on 6/18/2017.
 */
public class RSSLastCheckConfig extends AbstractConfig<Long, GlobalConfigurable> {
    public RSSLastCheckConfig() {
        super("rss_last_check", BotRole.BOT_ADMIN, 0L, "The last rss check in millis");
    }
    public static long getAndUpdate(){
        AtomicLong val = new AtomicLong();
        ConfigHandler.changeSetting(RSSLastCheckConfig.class, GlobalConfigurable.GLOBAL, aLong -> {
            val.set(aLong);
            return System.currentTimeMillis();
        });
        return val.get();
    }
    public boolean checkDefault(){
        return false;
    }
}
