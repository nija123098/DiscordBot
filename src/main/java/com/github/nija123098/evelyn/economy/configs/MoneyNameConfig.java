package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class MoneyNameConfig extends AbstractConfig<String, Configurable> {
    public MoneyNameConfig() {
        super("money_name", BotRole.BOT_ADMIN, "cookies", "The name of money");
    }
}
