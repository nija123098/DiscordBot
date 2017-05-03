package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.economy.MoneyTransfer;
import com.github.kaaz.emily.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class MoneyHistoryConfig extends AbstractConfig<List<MoneyTransfer>, Configurable> {
    public MoneyHistoryConfig() {
        super("money_history", BotRole.SYSTEM, new ArrayList<>(0), "The history of money");
    }
}
