package com.github.nija123098.evelyn.information.descriptions;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class UserDescriptionConfig extends AbstractConfig<String, User> {
    public UserDescriptionConfig() {
        super("user_description", "", ConfigCategory.STAT_TRACKING, "This user has no information set", "Information about the user that overrides per server");
    }
}
