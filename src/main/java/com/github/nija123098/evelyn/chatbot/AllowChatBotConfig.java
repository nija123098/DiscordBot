package com.github.nija123098.evelyn.chatbot;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class AllowChatBotConfig extends AbstractConfig<Boolean, Channel> {
    public AllowChatBotConfig() {
        super("allow_unprompted_chat", BotRole.GUILD_TRUSTEE, "If the bot may chat in a channel", Channel::isPrivate);
    }
}
