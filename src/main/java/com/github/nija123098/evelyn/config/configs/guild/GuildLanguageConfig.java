package com.github.nija123098.evelyn.config.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exception.ArgumentException;

import static com.github.nija123098.evelyn.config.ConfigCategory.GUILD_PERSONALIZATION;
import static com.github.nija123098.evelyn.util.LangString.*;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildLanguageConfig extends AbstractConfig<String, Guild> {
    public GuildLanguageConfig() {
        super("guild_language", "Guild Language", GUILD_PERSONALIZATION, (String) null, "The language the bot uses to communicate in the guild");
    }

    @Override
    protected String validateInput(Guild configurable, String v) {
        if (isLangName(v)) v = getLangCode(v);
        if (!isLangCode(v)) throw new ArgumentException("Please input a valid language code or name");
        return v;
    }

    @Override
    public String wrapTypeOut(String s, Guild configurable) {
        return getLangName(s);
    }
}
