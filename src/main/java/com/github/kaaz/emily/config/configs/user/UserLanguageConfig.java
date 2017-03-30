package com.github.kaaz.emily.config.configs.user;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 3/18/2017.
 */
public class UserLanguageConfig extends AbstractConfig<String, User> {
    public UserLanguageConfig() {
        super("user_language", BotRole.BANNED, null, "The language the bot uses to communicate with the user");
    }
}
