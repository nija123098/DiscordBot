package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 7/26/2017.
 */
public class BotChannelConfig extends AbstractConfig<Channel, Guild> {
    public BotChannelConfig() {
        super("bot_channel", BotRole.GUILD_TRUSTEE, "Channel where the bot's output goes to", Guild::getGeneralChannel);
    }
    public static Channel get(Guild guild){
        return ConfigHandler.getSetting(BotChannelConfig.class, guild);
    }
}
