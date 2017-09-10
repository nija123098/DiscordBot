package com.github.nija123098.evelyn.helping.presence;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 6/21/2017.
 */
public class SelfMarkedAwayConfig extends AbstractConfig<Boolean, User> {
    public SelfMarkedAwayConfig() {
        super("self_marked_afk", BotRole.USER, false, "The self marked version of if a user is away, for the bot to say so when this user is mentioned.  Millis since epoch.");
    }
}
