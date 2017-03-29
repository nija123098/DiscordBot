package com.github.kaaz.emily.config.configs.guild;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 3/29/2017.
 */
public class GuildPrefixConfig extends AbstractConfig<String, String, Guild> {
    public GuildPrefixConfig() {
        super("guild_prefix", BotRole.GUILD_TRUSTEE, "!", "The prefix for the guild");
    }
}
