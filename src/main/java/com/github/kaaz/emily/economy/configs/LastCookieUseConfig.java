package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class LastCookieUseConfig extends AbstractConfig<Long, User> {
    public LastCookieUseConfig() {// todo make delete at cookie hold max
        super("last_cookie_use", BotRole.BOT_ADMIN, 0L, "The last time the cookie command was used");
    }
    @Override
    public boolean checkDefault() {
        return false;
    }
}
