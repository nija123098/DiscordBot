package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * Made by nija123098 on 7/26/2017.
 */
public class BotChannelConfig extends AbstractConfig<Channel, Guild> {
    public BotChannelConfig() {
        super("bot_channel", ConfigCategory.LOGGING, guild -> guild.getChannels().stream().filter(channel -> {
            String name = FormatHelper.filtering(channel.getName(), Character::isLetter).toLowerCase();
            return name.contains("bot") || name.contains("spam") || name.contains("command") || name.contains("music") || name.contains("testing");
        }).findFirst().orElse(null), "Channel where the bot's output goes to");
    }
    public static Channel get(Guild guild){
        return ConfigHandler.getSetting(BotChannelConfig.class, guild);
    }
}
