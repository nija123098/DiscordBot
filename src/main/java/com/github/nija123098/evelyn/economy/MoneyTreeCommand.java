package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 7/7/2017.
 */
public class MoneyTreeCommand extends AbstractCommand {
    public MoneyTreeCommand() {
        super("moneytree", BotRole.BOT_ADMIN, ModuleLevel.ECONOMY, "mt", null, "Cookies do grow on trees");
    }
    @Command
    public void handle(@Argument Integer amount, @Argument User user){
        ConfigHandler.changeSetting(CurrentCurrencyConfig.class, user, aFloat -> aFloat + amount);
    }
}
