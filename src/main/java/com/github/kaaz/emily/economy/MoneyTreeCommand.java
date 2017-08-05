package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.economy.configs.CurrentMoneyConfig;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 7/7/2017.
 */
public class MoneyTreeCommand extends AbstractCommand {
    public MoneyTreeCommand() {
        super("moneytree", BotRole.BOT_ADMIN, ModuleLevel.ECONOMY, "mt", null, "Gives free money");
    }
    @Command
    public void handle(@Argument Integer amount, @Argument Configurable configurable){
        ConfigHandler.changeSetting(CurrentMoneyConfig.class, configurable, aFloat -> aFloat + amount);
    }
}
