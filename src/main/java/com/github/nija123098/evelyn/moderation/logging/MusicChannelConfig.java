package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import static com.github.nija123098.evelyn.config.ConfigCategory.LOGGING;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MusicChannelConfig extends AbstractConfig<Channel, Guild> {
    public MusicChannelConfig() {
        super("music_channel", "Music Channel", LOGGING, BotChannelConfig::get, "The channel Evelyn prints the current playing music to.");
    }
}
