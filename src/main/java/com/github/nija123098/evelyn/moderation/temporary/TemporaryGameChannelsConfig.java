package com.github.nija123098.evelyn.moderation.temporary;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TemporaryGameChannelsConfig extends AbstractConfig<Integer, Guild> {
    public TemporaryGameChannelsConfig() {
        super("temp_game_users", "Co-op Channel Threshold", ConfigCategory.GAME_TEMPORARY_CHANNELS, 0, "Makes a channel for when people are playing the same game 0 disables it");
    }
}
