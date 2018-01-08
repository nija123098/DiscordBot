package com.github.nija123098.evelyn.config.configs.user;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 6/23/2017.
 */
public class VoicePrefixConfig extends AbstractConfig<String, User> {
    public VoicePrefixConfig() {
        super("current_money", "voice_prefix", ConfigCategory.PERSONAL_PERSONALIZATION, "command", "The prefix Emily responds to in voice channels");
    }
}
