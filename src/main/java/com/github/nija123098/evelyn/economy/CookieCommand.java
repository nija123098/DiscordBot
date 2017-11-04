package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrentMoneyConfig;
import com.github.nija123098.evelyn.economy.configs.LastCookieUseConfig;
import com.github.nija123098.evelyn.economy.configs.MoneyNameConfig;
import com.github.nija123098.evelyn.economy.configs.MoneySymbolConfig;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class CookieCommand extends AbstractCommand {
    public CookieCommand() {
        super("cookie", ModuleLevel.ECONOMY, "cookies, candy, dailies", "cookie", "Gives you a cookie");
    }
    @Command
    public void command(Guild guild, User user, MessageMaker maker){
        Long time = ConfigHandler.getSetting(LastCookieUseConfig.class, user);
        int count = Math.min((int) ((System.currentTimeMillis() - time) / 7_200_000), 12);
        if (count != 12) time += 7_200_000 * count;
        else time = System.currentTimeMillis();
        String name = ConfigHandler.getSetting(MoneyNameConfig.class, guild), symbol = ConfigHandler.getSetting(MoneySymbolConfig.class, guild);
        if (count == 0) maker.append("No " + symbol + " for you yet");
        else maker.append("You get " + count + " " + name + "\nYou now have " + (ConfigHandler.getSetting(CurrentMoneyConfig.class, user) + count)).appendRaw(" " + symbol);
        maker.append("\nYou can retrieve a " + name + " every 120 minutes, I'll save up to 12 for you");
        if (count != 0) MoneyTransfer.transact(guild, user, null, count, "Cookies from the cookie oven!");
        ConfigHandler.setSetting(LastCookieUseConfig.class, user, time);
    }
    @Override
    public long getCoolDown(Class<? extends Configurable> clazz) {
        if (clazz.equals(User.class)) return 600_000;
        return super.getCoolDown(clazz);
    }
}
