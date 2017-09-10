package com.github.nija123098.evelyn.config.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.LangString;

/**
 * Made by nija123098 on 3/18/2017.
 */
public class GuildLanguageConfig extends AbstractConfig<String, Guild> {
    public GuildLanguageConfig() {
        super("guild_language", BotRole.GUILD_TRUSTEE, null, "The language the bot uses to communicate in the guild");
    }
    @Override
    protected void validateInput(Guild configurable, String v) {
        if (!LangString.isLangCode(v) && !LangString.isLangName(v)) throw new ArgumentException("Please input a valid language code or name");
    }
    @Override
    public String wrapTypeIn(String e, Guild configurable) {
        return LangString.isLangCode(e) ? e : LangString.getLangCode(e);
    }
    @Override
    public String wrapTypeOut(String s, Guild configurable) {
        return LangString.getLangName(s);
    }
}
