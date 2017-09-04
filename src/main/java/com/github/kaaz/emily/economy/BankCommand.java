package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GuildUser;
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
        super("bank", ModuleLevel.ECONOMY, "currency, money, jar", null, "Gets the current balance of the user");
    }
    @Command
    public void command(@Argument(info = "user",optional = true) Configurable configurable, User user, @Context(softFail = true) GuildUser guildUser, MessageMaker maker){
        if (configurable == null) {
            configurable = user;
            getFormat(maker, "Your bank contains ", configurable);
        } else {
            getFormat(maker, "" + configurable.getName() + "'s bank contains ", configurable);
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
