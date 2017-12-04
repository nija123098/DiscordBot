package com.github.nija123098.evelyn.perms.configs.standard;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GlobalBotRoleConfig extends AbstractConfig<Set<BotRole>, User> {
    public GlobalBotRoleConfig() {
        super("global_flag_ranks", "", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "The config for if an user is a contributor to the bot");
    }
}
