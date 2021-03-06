package com.github.nija123098.evelyn.helping.reward;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
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
        super(RewardCommand.class, "bug", null, null, "b", "Reward a user for reporting a bug.");
    }

    @Command
    public void bug(MessageMaker maker, Channel channel, @Argument User user, @Argument Integer amount, @Argument(optional = true, replacement = ContextType.NONE) String s) {
        MessageMaker maker2 = new MessageMaker(channel);
        maker.withChannel(user.getOrCreatePMChannel());
        maker.withColor(new Color(175, 30, 5));
        maker.getTitle().clear().append("🎁 Evelyn Reward");
        maker.withThumb(ConfigProvider.URLS.bugThumb());
        maker.withTimestamp(System.currentTimeMillis());
        maker.getNote().clear().append("Reward: " + ConfigHandler.getSetting(CurrencySymbolConfig.class, GlobalConfigurable.GLOBAL) + " " + amount);
        ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, ConfigHandler.getSetting(CurrentCurrencyConfig.class, user) + amount);
        maker.getHeader().clear().append("We received a bug report from you that was very useful to us! Here is a reward for your find.");
        if (!s.isEmpty()){
            maker.getNewFieldPart().withBoth("Dev Message", s).withInline(false);
            maker2.getNewFieldPart().withBoth("Dev Message", s).withInline(false);
        }
        maker.withDM();
        maker2.getTitle().clear().append("🐞 Bug Report reward sent.");
        maker2.getNote().clear().append("Reward: " + ConfigHandler.getSetting(CurrencySymbolConfig.class, GlobalConfigurable.GLOBAL) + " " + amount + " \u200b \u200b User: " + user.getNameAndDiscrim() + " | " + user.getID());
        maker2.withThumb(ConfigProvider.URLS.rewardThumb());
        maker2.withTimestamp(System.currentTimeMillis());
        maker2.send();
    }
}