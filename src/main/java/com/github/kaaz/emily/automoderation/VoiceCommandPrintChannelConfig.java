package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/23/2017.
 */
public class VoiceCommandPrintChannelConfig extends AbstractConfig<Channel, Guild> {
    public VoiceCommandPrintChannelConfig() {
        super("voice_text_channel", BotRole.GUILD_TRUSTEE, "The channel in which commands from voice channels are printed if they are to large to say", Guild::getGeneralChannel);
    }
}
