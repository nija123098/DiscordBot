package com.github.kaaz.emily.config.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class AllowChatBotConfig extends AbstractConfig<Boolean, Channel> {
    public AllowChatBotConfig() {
        super("allow_chat", BotRole.GUILD_TRUSTEE, false, "If the bot may chat in a channel");
    }
    @Override
    public Boolean getValue(Channel configurable) {
        return configurable.isPrivate() ? true : super.getValue(configurable);
    }
}
