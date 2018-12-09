package com.github.nija123098.evelyn.discordobjects.helpers.profileimage;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

public class UserAvatarHashConfig extends AbstractConfig<String, User> {
    public UserAvatarHashConfig() {
        super("user_avatar_hash", "", ConfigCategory.STAT_TRACKING, (String) null, "The avitar hash for the user");
    }
}
