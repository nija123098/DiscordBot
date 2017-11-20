package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 7/26/2017.
 */
public class MusicChannelConfig extends AbstractConfig<Channel, Guild> {
    public MusicChannelConfig() {
        super("music_channel", "", ConfigCategory.LOGGING, BotChannelConfig::get, "The channel Evelyn prints the current playing music to.");
    }
}
