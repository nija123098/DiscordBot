package com.github.nija123098.evelyn.helping.embed;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class LastEmbedConfig extends AbstractConfig<Message, GuildUser> {
    public LastEmbedConfig() {
        super("last_embed", "", ConfigCategory.STAT_TRACKING, (Message) null, "stores the last editable embed");
    }
}