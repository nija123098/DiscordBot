package com.github.nija123098.evelyn.economy.bank;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.economy.configs.CurrencyBonusConfig;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class SetBonusCommand extends AbstractCommand {

    public SetBonusCommand() {
        super("setbonus", ModuleLevel.BOT_ADMINISTRATIVE, "sb", null, "set the global bonus value");
    }

    @Command
    public void command(@Argument Integer integer, MessageMaker maker) {
        ConfigHandler.setSetting(CurrencyBonusConfig.class, GlobalConfigurable.GLOBAL, integer);
        maker.getTitle().appendRaw("Bonus set");
        maker.appendRaw("\u200b\nSet bonus to `" + integer + "`");
    }
}