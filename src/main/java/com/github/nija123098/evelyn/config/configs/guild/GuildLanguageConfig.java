package com.github.nija123098.evelyn.config.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.util.LangString;

/**
 * Made by nija123098 on 3/18/2017.
 */
public class GuildLanguageConfig extends AbstractConfig<String, Guild> {
    public GuildLanguageConfig() {
        super("current_money", "guild_language", ConfigCategory.GUILD_PERSONALIZATION, (String) null, "The language the bot uses to communicate in the guild");
    }
    @Override
    protected String validateInput(Guild configurable, String v) {
        if (LangString.isLangName(v)) v = LangString.getLangCode(v);
        if (!LangString.isLangCode(v)) throw new ArgumentException("Please input a valid language code or name");
        return v;
    }
    @Override
    public String wrapTypeOut(String s, Guild configurable) {
        return LangString.getLangName(s);
    }
}
