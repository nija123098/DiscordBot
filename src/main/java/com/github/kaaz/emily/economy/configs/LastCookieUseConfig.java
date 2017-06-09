package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class LastCookieUseConfig extends AbstractConfig<Long, GuildUser> {
    public LastCookieUseConfig() {
        super("last_cookie_use", BotRole.BOT_ADMIN, null, "The last time the cookie command was used");
    }
}
