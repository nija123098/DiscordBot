package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class MoneyNameConfig extends AbstractConfig<String, Configurable> {
    public MoneyNameConfig() {
        super("money_name", BotRole.BOT_ADMIN, "cookies", "The name of money");
    }
}
