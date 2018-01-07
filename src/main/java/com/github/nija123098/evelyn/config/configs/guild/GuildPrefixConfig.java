package com.github.nija123098.evelyn.config.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import static com.github.nija123098.evelyn.config.ConfigCategory.GUILD_PERSONALIZATION;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildPrefixConfig extends AbstractConfig<String, Guild> {
    public GuildPrefixConfig() {
        super("guild_prefix", "Bot Prefix", GUILD_PERSONALIZATION, "!", "The prefix for the guild");
    }

    @Override
    public String setValue(Guild configurable, String value, boolean overrideCache) {
        return super.setValue(configurable, value, true);
    }

    @Override
    public String getValue(Guild configurable, boolean overrideCache) {
        return super.getValue(configurable, true);
    }
}
