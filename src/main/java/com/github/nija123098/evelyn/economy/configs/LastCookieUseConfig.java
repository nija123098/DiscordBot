package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class LastCookieUseConfig extends AbstractConfig<Long, User> {
    public LastCookieUseConfig() {
        super("last_cookie_use", ConfigCategory.STAT_TRACKING, 0L, "The last time the cookie command was used");
    }
}
