package com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;

public class RebootInVoiceChannelConfig extends AbstractConfig<VoiceChannel, Guild> {
    public RebootInVoiceChannelConfig() {
        super("reboot_in_voice_channel", "", ConfigCategory.STAT_TRACKING, (VoiceChannel) null, "The channel the bot was in when it needed to reboot");
    }
}
