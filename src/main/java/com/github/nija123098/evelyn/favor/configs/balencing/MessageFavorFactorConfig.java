package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class MessageFavorFactorConfig extends AbstractConfig<Float, Guild> {
    public MessageFavorFactorConfig() {
        super("message_favor_factor", ConfigCategory.FAVOR, 1F, "The factor by which favor is bestowed on a guild user for a message");
    }
}
