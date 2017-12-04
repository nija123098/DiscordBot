package com.github.nija123098.evelyn.config.configs.user;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ArgumentException;

import static com.github.nija123098.evelyn.config.ConfigCategory.PERSONAL_PERSONALIZATION;
import static com.github.nija123098.evelyn.util.LangString.*;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class UserLanguageConfig extends AbstractConfig<String, User> {
    public UserLanguageConfig() {
        super("user_language", "", PERSONAL_PERSONALIZATION, (String) null, "The language the bot uses to communicate with the user");
    }

    @Override
    protected String validateInput(User configurable, String v) {
        if (isLangName(v)) v = getLangCode(v);
        if (!isLangCode(v)) throw new ArgumentException("Please input a valid language code or name");
        return v;
    }

    @Override
    public String wrapTypeOut(String s, User configurable) {
        return getLangName(s);
    }
}
