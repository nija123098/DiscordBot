package com.github.nija123098.evelyn.moderation.gameroles;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

public class UserHasGameRoleConfig extends AbstractConfig<Boolean, User> {
    public UserHasGameRoleConfig() {
        super("user_has_game_role", "", ConfigCategory.STAT_TRACKING, false, "If the user has been assigned a game role");
    }
}
