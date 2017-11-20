package com.github.nija123098.evelyn.helping.presence;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 6/21/2017.
 */
public class SelfMarkedAwayConfig extends AbstractConfig<Boolean, User> {
    public SelfMarkedAwayConfig() {
        super("self_marked_afk", "", ConfigCategory.PERSONAL_PERSONALIZATION, false, "The self marked version of if a user is away, for the bot to say so when this user is mentioned.  Millis since epoch.");
    }
}
