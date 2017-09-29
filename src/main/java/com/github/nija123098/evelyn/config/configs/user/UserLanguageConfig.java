package com.github.nija123098.evelyn.config.configs.user;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.LangString;

/**
 * Made by nija123098 on 3/18/2017.
 */
public class UserLanguageConfig extends AbstractConfig<String, User> {
    public UserLanguageConfig() {
        super("user_language", ConfigCategory.PERSONAL_PERSONALIZATION, (String) null, "The language the bot uses to communicate with the user");
    }
    @Override
    protected String validateInput(User configurable, String v) {
        if (LangString.isLangName(v)) v = LangString.getLangCode(v);
        if (!LangString.isLangCode(v)) throw new ArgumentException("Please input a valid language code or name");
        return v;
    }
    @Override
    public String wrapTypeOut(String s, User configurable) {
        return LangString.getLangName(s);
    }
}
