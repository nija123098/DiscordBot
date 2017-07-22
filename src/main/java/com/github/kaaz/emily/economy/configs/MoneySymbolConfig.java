package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.EmoticonHelper;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class MoneySymbolConfig extends AbstractConfig<String, Configurable> {
    public MoneySymbolConfig() {
        super("money_symbol", BotRole.BOT_ADMIN, EmoticonHelper.getChars("cookie", true), "The symbol money is represented by");
    }
}
