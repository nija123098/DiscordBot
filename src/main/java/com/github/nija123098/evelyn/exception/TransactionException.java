package com.github.nija123098.evelyn.exception;

import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.currencytransfer.ItemComponent;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TransactionException extends UserIssueException {// todo reconcile with insufficient funds exception
    public TransactionException(Configurable configurable, int inefficient) {
        super(configurable.getName() + " does not have enough money to complete the transaction.  They lack " + inefficient + ConfigHandler.getSetting(CurrencySymbolConfig.class, configurable));
    }

    public TransactionException(Configurable configurable, ItemComponent itemComponent, int count) {
        super(configurable.getName() + " needs " + count + " more " + itemComponent.name() + "s");
    }
}
