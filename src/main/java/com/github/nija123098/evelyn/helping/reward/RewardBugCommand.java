package com.github.nija123098.evelyn.helping.reward;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;

import java.awt.*;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class RewardBugCommand extends AbstractCommand {
    public RewardBugCommand() {
        super(RewardCommand.class,"bug", null, null, "b", "Reward a user for reporting a bug.");
    }

    @Command
    public void bug(MessageMaker maker, @Argument User user, @Argument Integer amount, @Argument(optional = true, replacement = ContextType.NONE) String s) {
        maker.withChannel(user.getOrCreatePMChannel());
        maker.withColor(new Color(175, 30, 5));
        maker.getTitle().clear().append("🎁 Evelyn Reward 🎁");
        maker.withTimestamp(System.currentTimeMillis());
        maker.getNote().clear().append("Reward: " + ConfigHandler.getSetting(CurrencySymbolConfig.class, GlobalConfigurable.GLOBAL) + " " + amount);
        ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, ConfigHandler.getSetting(CurrentCurrencyConfig.class, user) + amount);
        maker.getHeader().clear().append("We received a bug report from you that was very useful to us! Here is a reward for your find.");
        if (!s.isEmpty()){
            maker.getNewFieldPart().withBoth("Dev Message", s).withInline(false);
        }
        maker.withDM();

    }
}