package com.github.nija123098.evelyn.config.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 3/29/2017.
 */
public class GuildPrefixConfig extends AbstractConfig<String, Guild> {
    public GuildPrefixConfig() {
        super("guild_prefix", BotRole.GUILD_TRUSTEE, "!", "The prefix for the guild");
    }
}
