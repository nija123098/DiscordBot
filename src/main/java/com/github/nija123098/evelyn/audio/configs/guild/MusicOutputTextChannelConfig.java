package com.github.nija123098.evelyn.audio.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class MusicOutputTextChannelConfig extends AbstractConfig<Channel, Guild> {
    public MusicOutputTextChannelConfig() {
        super("music_channel", BotRole.GUILD_TRUSTEE, null, "The channel current music is sent to");
    }
}
