package com.github.nija123098.evelyn.helping.embed;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class LastEmbedMillisConfig extends AbstractConfig<Long, GuildUser> {
    public LastEmbedMillisConfig() {
        super("last_embed_millis", "", ConfigCategory.STAT_TRACKING, 0L , "the millis of the last embed sent by embed command");
    }
}