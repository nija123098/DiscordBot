package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class MoneySymbolConfig extends AbstractConfig<String, Configurable> {
    public MoneySymbolConfig() {
        super("money_symbol", BotRole.BOT_ADMIN, EmoticonHelper.getChars("cookie", true), "The symbol money is represented by");
    }
}
