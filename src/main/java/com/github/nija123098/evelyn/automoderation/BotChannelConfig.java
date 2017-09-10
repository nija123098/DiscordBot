package com.github.nija123098.evelyn.automoderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

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