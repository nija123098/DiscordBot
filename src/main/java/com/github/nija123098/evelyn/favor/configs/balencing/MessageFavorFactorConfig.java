package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MessageFavorFactorConfig extends AbstractConfig<Float, Guild> {
    public MessageFavorFactorConfig() {
        super("message_favor_factor", "Message Favor Factor", ConfigCategory.FAVOR, 1F, "The factor by which favor is bestowed on a guild user for a message");
    }
}
