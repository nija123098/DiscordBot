package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class PrefixMoneySymbolConfig extends AbstractConfig<Boolean, Configurable> {
    public PrefixMoneySymbolConfig() {
        super("prefix_money_symbol", BotRole.BOT_ADMIN, false, "If the money symbol appears before or after the amount");
    }
}
