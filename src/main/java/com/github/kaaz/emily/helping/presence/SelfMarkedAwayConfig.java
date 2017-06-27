package com.github.kaaz.emily.helping.presence;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/21/2017.
 */
public class SelfMarkedAwayConfig extends AbstractConfig<Boolean, User> {
    public SelfMarkedAwayConfig() {
        super("self_marked_afk", BotRole.USER, false, "The self marked version of if a user is away, for the bot to say so when this user is mentioned.  Millis since epoch.");
    }
}
