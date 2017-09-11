package com.github.nija123098.evelyn.moderation.temporarychannels;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.perms.BotRole;

public class TemporaryChannelConfig extends AbstractConfig<Boolean, Channel> {
    public TemporaryChannelConfig() {
        super("temporary_channel", BotRole.GUILD_TRUSTEE, false, "This channel will be deleted when there is no activity for a while");
    }
}
