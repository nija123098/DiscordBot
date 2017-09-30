package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrentMoneyConfig;
import com.github.nija123098.evelyn.economy.configs.MoneySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.PrefixMoneySymbolConfig;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class BankCommand extends AbstractCommand {
    public BankCommand() {
        super("bank", ModuleLevel.ECONOMY, "currency, money, jar", null, "Gets the current balance of the user");
    }
    @Command
    public void command(@Argument(info = "user", optional = true) Configurable configurable, User user, @Context(softFail = true) GuildUser guildUser, MessageMaker maker){
        if (configurable == null) {
            configurable = user;
            getFormat(maker, "Your bank contains ", configurable);
        } else {
            getFormat(maker, configurable.getName() + "'s bank contains ", configurable);
        }
        if (user.equals(configurable) && guildUser != null){
            getFormat(maker, "\nIn this server you have ", guildUser);
        }
    }
    private static void getFormat(MessageMaker maker, String prefix, Configurable configurable){
        String symbol = ConfigHandler.getSetting(MoneySymbolConfig.class, configurable.getGoverningObject());
        String amount = ConfigHandler.getSetting(CurrentMoneyConfig.class, configurable) + "";
        if (ConfigHandler.getSetting(PrefixMoneySymbolConfig.class, configurable.getGoverningObject())) symbol += amount;
        else symbol = amount + symbol;
        maker.appendAlternate(false, prefix, symbol);// symbol appended to
    }
}
