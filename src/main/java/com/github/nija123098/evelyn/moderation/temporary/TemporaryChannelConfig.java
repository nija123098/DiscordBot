package com.github.nija123098.evelyn.moderation.temporary;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

public class TemporaryChannelConfig extends AbstractConfig<Boolean, Channel> {
    public TemporaryChannelConfig() {
        super("temporary_channel", ConfigCategory.GAME_TEMPORARY_CHANNELS, false, "This channel will be deleted when there is no activity for a while");
    }
}
