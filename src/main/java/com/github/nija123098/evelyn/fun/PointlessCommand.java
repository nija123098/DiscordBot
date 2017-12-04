package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class PointlessCommand extends AbstractCommand {
    public PointlessCommand() {
        super("pointless", ModuleLevel.FUN, null, null, "lel");
    }

    private static int currencyLost = 0;

    @Command
    public void command(User user, MessageMaker maker) {
        maker.appendRaw(EmoticonHelper.getEmoji("EMPTY").toString());
        maker.withReactionBehavior("red_circle", (add, reaction, user1) -> {
            int currency = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user);
            if (currency > 0) {
                currencyLost = currencyLost + 1;
                if (currencyLost >= 10) {
                    currencyLost = 0;
                    new MessageMaker(user).mustEmbed().appendRaw(FormatHelper.embedLink("you may need help", "http://www.smartrecovery.org/addiction/gambling_addiction.html")).withDM().send();
                }
                ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, (currency - 1));
            }
        });
    }
}
