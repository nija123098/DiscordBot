package com.github.nija123098.evelyn.config.configs.user;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import static com.github.nija123098.evelyn.config.ConfigCategory.PERSONAL_PERSONALIZATION;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class VoicePrefixConfig extends AbstractConfig<String, User> {
    public VoicePrefixConfig() {
        super("voice_prefix", "", PERSONAL_PERSONALIZATION, "command", "The prefix Evelyn responds to in voice channels");
    }
}
