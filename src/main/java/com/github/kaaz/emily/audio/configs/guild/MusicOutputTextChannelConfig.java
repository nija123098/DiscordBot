package com.github.kaaz.emily.audio.configs.guild;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class MusicOutputTextChannelConfig extends AbstractConfig<Channel, Guild> {
    public MusicOutputTextChannelConfig() {
        super("music_channel", BotRole.GUILD_TRUSTEE, null, "The channel current music is sent to");
    }
}
