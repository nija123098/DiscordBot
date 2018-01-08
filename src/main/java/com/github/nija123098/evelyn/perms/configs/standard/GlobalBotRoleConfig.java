package com.github.nija123098.evelyn.perms.configs.standard;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 2/22/2017.
 */
public class GlobalBotRoleConfig extends AbstractConfig<Set<BotRole>, User> {
    public GlobalBotRoleConfig() {
        super("current_money", "global_flag_ranks", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "The config for if an user is a contributor to the bot");
    }
}
