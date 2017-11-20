package com.github.nija123098.evelyn.chatbot;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class AllowChatBotConfig extends AbstractConfig<Boolean, Channel> {
    public AllowChatBotConfig() {
        super("allow_unprompted_chat", "", ConfigCategory.GUILD_PERSONALIZATION, Channel::isPrivate, "If the bot may chat in a channel");
    }
}
