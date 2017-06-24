package com.github.kaaz.emily.config.configs.user;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/23/2017.
 */
public class VoicePrefixConfig extends AbstractConfig<String, User> {
    public VoicePrefixConfig() {
        super("voice_prefix", BotRole.USER, "command", "The prefix Emily responds to in voice channels");
    }
}
