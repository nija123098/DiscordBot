package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * Written by Soarnir 23/11/17
 */

public class PointlessCommand extends AbstractCommand {
    public PointlessCommand() {
        super("pointless", ModuleLevel.FUN, null, null, "lel");
    }

    @Command
    public void command(User user, MessageMaker maker) {
        maker.appendRaw(EmoticonHelper.getEmoji("EMPTY").toString());
        maker.withReactionBehavior("red_circle", (add, reaction, user1) -> {
            int currency = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user);
            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, (currency - 1));
        });
    }
}
