package com.github.nija123098.evelyn.config.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class GuildIgnoredConfig extends AbstractConfig<Boolean, Guild> {
    public GuildIgnoredConfig() {
        super("guild_ignore", "Guild Ignored", ConfigCategory.STAT_TRACKING, false, "Whether a guild should be ignored for stats and optimization");
    }
}
