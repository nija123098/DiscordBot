package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import static com.github.nija123098.evelyn.config.ConfigCategory.LOGGING;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class VoiceCommandPrintChannelConfig extends AbstractConfig<Channel, Guild> {
    public VoiceCommandPrintChannelConfig() {
        super("voice_text_channel", "Voice Text Channel", LOGGING, BotChannelConfig::get, "The channel in which commands from voice channels are printed if they are to large to say");
    }
}
