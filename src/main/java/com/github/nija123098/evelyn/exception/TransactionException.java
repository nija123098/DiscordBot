package com.github.nija123098.evelyn.exception;

import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.currencytransfer.ItemComponent;

import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.discordobjects.wrappers.Guild.getGuild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TransactionException extends BotException {
    public TransactionException(Configurable configurable, float inefficient) {
        super(configurable.getName() + " does not have enough money to complete the transaction.  They lack " + inefficient + getSetting(CurrencySymbolConfig.class, getGuild(configurable.getID())));
    }

    public TransactionException(Configurable configurable, ItemComponent itemComponent, int count) {
        super(configurable.getName() + " needs " + count + " more " + itemComponent.name() + "s");
    }
}
