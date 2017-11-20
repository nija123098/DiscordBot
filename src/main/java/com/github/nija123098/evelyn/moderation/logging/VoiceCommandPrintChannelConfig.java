package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 6/23/2017.
 */
public class VoiceCommandPrintChannelConfig extends AbstractConfig<Channel, Guild> {
    public VoiceCommandPrintChannelConfig() {
        super("voice_text_channel", "", ConfigCategory.LOGGING, BotChannelConfig::get, "The channel in which commands from voice channels are printed if they are to large to say");
    }
}
