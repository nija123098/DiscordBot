package com.github.kaaz.emily.helping.poll;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/15/2017.
 */
public class PollIDCountConfig extends AbstractConfig<Integer, GlobalConfigurable> {
    public PollIDCountConfig() {// default to exceed max options to avoid accidental mix of option and poll ID
        super("poll_id_count", BotRole.BOT_ADMIN, 10, "The count of poll IDs to avoid duplication.");
    }
    static synchronized int getNewID(){
        return ConfigHandler.changeSetting(PollIDCountConfig.class, GlobalConfigurable.GLOBAL, integer -> integer + 1);
    }
}
