package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.economy.configs.CurrentMoneyConfig;
import com.github.kaaz.emily.economy.configs.MoneySymbolConfig;
import com.github.kaaz.emily.economy.configs.PrefixMoneySymbolConfig;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class BankCommand extends AbstractCommand {
    public BankCommand() {
        super("bank", ModuleLevel.ECONOMY, "currency, money, jar", null, "Gets the current balance");
    }
    @Command
    public void command(@Argument(optional = true) Configurable configurable, User user, MessageMaker maker){
        if (configurable == null) configurable = user;
        String symbol = ConfigHandler.getSetting(MoneySymbolConfig.class, configurable.getGoverningObject());
        String amount = ConfigHandler.getSetting(CurrentMoneyConfig.class, configurable) + "";
        if (ConfigHandler.getSetting(PrefixMoneySymbolConfig.class, configurable.getGoverningObject())) symbol += amount;
        else symbol = amount + symbol;
        maker.appendAlternate(true, configurable.getName() + " ", " has ", symbol);
    }
}
