package com.github.nija123098.evelyn.config.configs.user;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 6/23/2017.
 */
public class VoicePrefixConfig extends AbstractConfig<String, User> {
    public VoicePrefixConfig() {
        super("voice_prefix", ConfigCategory.PERSONAL_PERSONALIZATION, "command", "The prefix Evelyn responds to in voice channels");
    }
}
