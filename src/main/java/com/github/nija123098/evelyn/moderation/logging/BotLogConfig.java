package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.util.FormatHelper;

public class BotLogConfig extends AbstractConfig<Channel, Guild> {
    public BotLogConfig() {
        super("bot_log", "Bot Log", ConfigCategory.LOGGING, guild -> guild.getChannels().stream().filter(Channel::canPost).filter(channel -> FormatHelper.filtering(channel.getName().toLowerCase(), Character::isLetter).contains("botlog")).findFirst().orElse(null), "Where the bot prints usages of meaningful commands to");
    }
}