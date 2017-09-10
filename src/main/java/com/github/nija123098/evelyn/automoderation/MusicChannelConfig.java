package com.github.nija123098.evelyn.automoderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 7/26/2017.
 */
public class MusicChannelConfig extends AbstractConfig<Channel, Guild> {
    public MusicChannelConfig() {
        super("music_channel", BotRole.GUILD_TRUSTEE, "The channel Emily prints the current playing music to.", BotChannelConfig::get);
    }
}
