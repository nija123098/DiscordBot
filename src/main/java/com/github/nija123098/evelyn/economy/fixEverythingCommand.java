package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class fixEverythingCommand extends AbstractCommand {

    //constructor
    public fixEverythingCommand() {
        super("fixeverything", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "thing fixer");
    }

    @Command
    public void command(MessageMaker maker) {
        for (User user : DiscordClient.getUsers()) {
            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, 2000);
        }
        maker.appendRaw("Fixed everything, sweeeet!");
    }
}