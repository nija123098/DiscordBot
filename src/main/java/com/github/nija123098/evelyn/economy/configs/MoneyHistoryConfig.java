package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.economy.MoneyTransfer;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class MoneyHistoryConfig extends AbstractConfig<List<MoneyTransfer>, Configurable> {
    public MoneyHistoryConfig() {
        super("money_history", BotRole.SYSTEM, new ArrayList<>(0), "The history of money");
    }
    public boolean checkDefault(){
        return false;
    }
}
