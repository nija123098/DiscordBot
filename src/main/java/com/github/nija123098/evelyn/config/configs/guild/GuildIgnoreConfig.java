package com.github.nija123098.evelyn.config.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class GuildIgnoreConfig extends AbstractConfig<List<Guild>, GlobalConfigurable> {
    public GuildIgnoreConfig() {
        super("guild_ignore", "Guild Ignore", ConfigCategory.STAT_TRACKING, new ArrayList<>(), "Whether a guild user count should be excluded from the stats command");
    }
}
