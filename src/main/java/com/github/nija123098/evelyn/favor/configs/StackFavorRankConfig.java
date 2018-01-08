package com.github.nija123098.evelyn.favor.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;

public class StackFavorRankConfig extends AbstractConfig<Boolean, Role> {
    public StackFavorRankConfig() {
        super("current_money", "stack_favor_rank", ConfigCategory.GUILD_PERSONALIZATION, true, "If the earning favor ranks stacks, otherwise only the highest will be kept");
    }
}
