package com.github.kaaz.emily.config.configs.guild;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 3/18/2017.
 */
public class GuildLanguageConfig extends AbstractConfig<String, Guild> {
    public GuildLanguageConfig() {
        super("guild_language", BotRole.GUILD_TRUSTEE, "en", "The language the bot uses to communicate in the guild");
    }
}
