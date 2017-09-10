package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class CurrentMoneyConfig extends AbstractConfig<Integer, Configurable> {
    public CurrentMoneyConfig() {
        super("current_money", BotRole.BOT_ADMIN, 0, "The amount of money a guild user has");
    }
    public boolean checkDefault(){
        return false;
    }
}
