package com.github.nija123098.evelyn.favor.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StackFavorRankConfig extends AbstractConfig<Boolean, Role> {
    public StackFavorRankConfig() {
        super("stack_favor_rank", "", ConfigCategory.GUILD_PERSONALIZATION, false, "If the earning favor ranks stacks, otherwise only the highest will be kept");
    }
}
