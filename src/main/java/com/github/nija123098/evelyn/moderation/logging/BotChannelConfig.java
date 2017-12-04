package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import static com.github.nija123098.evelyn.config.ConfigCategory.LOGGING;
import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.util.FormatHelper.filtering;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class BotChannelConfig extends AbstractConfig<Channel, Guild> {
    public BotChannelConfig() {
        super("bot_channel", "Bot Channel", LOGGING, guild -> guild.getChannels().stream().filter(channel -> {
            String name = filtering(channel.getName(), Character::isLetter).toLowerCase();
            return name.contains("bot") || name.contains("spam") || name.contains("command") || name.contains("music") || name.contains("testing");
        }).findFirst().orElse(null), "Channel where the bot's output goes to");
    }

    public static Channel get(Guild guild) {
        return getSetting(BotChannelConfig.class, guild);
    }
}
