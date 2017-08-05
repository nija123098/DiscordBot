package com.github.kaaz.emily.exeption;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.economy.ItemComponent;
import com.github.kaaz.emily.economy.configs.MoneySymbolConfig;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class TransactionException extends BotException {
    public TransactionException(Configurable configurable, float inefficient){
        super(configurable.getName() + " does not have enough money to complete the transaction.  They lack " + inefficient + ConfigHandler.getSetting(MoneySymbolConfig.class, configurable.getGoverningObject()));
        this.printStackTrace();
    }
    public TransactionException(Configurable configurable, ItemComponent itemComponent, int count){
        super(configurable.getName() + " needs " + count + " more " + itemComponent.name() + "s");
    }
}
