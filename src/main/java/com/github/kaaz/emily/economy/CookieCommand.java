package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.economy.configs.LastCookieUseConfig;
import com.github.kaaz.emily.economy.configs.MoneyNameConfig;
import com.github.kaaz.emily.economy.configs.MoneySymbolConfig;

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
        if (count == 0) maker.append("No " + ConfigHandler.getSetting(MoneySymbolConfig.class, user.getGoverningObject()) + " for you yet");
        else maker.append("You get " + count + " " + ConfigHandler.getSetting(MoneyNameConfig.class, user.getGoverningObject()));
        maker.append("\nYou can retrieve a " + ConfigHandler.getSetting(MoneyNameConfig.class, user.getGoverningObject()) + " every 120 minutes, I'll save up to 12 for you");
        if (count != 0) MoneyTransfer.transact(guild, user, null, count, "Cookies from the cookie oven!");
        ConfigHandler.setSetting(LastCookieUseConfig.class, user, time);
    }
}
