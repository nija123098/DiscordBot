package com.github.nija123098.evelyn.exception;

import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.economy.ItemComponent;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class TransactionException extends BotException {
    public TransactionException(Configurable configurable, float inefficient){
        super(configurable.getName() + " does not have enough money to complete the transaction.  They lack " + inefficient + ConfigHandler.getSetting(CurrencySymbolConfig.class, Guild.getGuild(configurable.getID())));
    }
    public TransactionException(Configurable configurable, ItemComponent itemComponent, int count){
        super(configurable.getName() + " needs " + count + " more " + itemComponent.name() + "s");
    }
}