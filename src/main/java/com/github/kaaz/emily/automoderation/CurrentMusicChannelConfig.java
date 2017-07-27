package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 7/26/2017.
 */
public class CurrentMusicChannelConfig extends AbstractConfig<Channel, Guild> {
    public CurrentMusicChannelConfig() {
        super("current_music_channel", BotRole.GUILD_TRUSTEE, "The channel Emily prints the current playing music to.", BotChannelConfig::get);
    }
}
