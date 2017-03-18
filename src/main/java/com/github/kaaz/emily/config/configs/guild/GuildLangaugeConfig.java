package com.github.kaaz.emily.config.configs.guild;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 3/18/2017.
 */
public class GuildLangaugeConfig extends AbstractConfig<String> {
    public GuildLangaugeConfig() {
        super("guild_language", BotRole.GUILD_TRUSTEE, "English", "The language the bot uses to communicate in the guild");
    }
}
