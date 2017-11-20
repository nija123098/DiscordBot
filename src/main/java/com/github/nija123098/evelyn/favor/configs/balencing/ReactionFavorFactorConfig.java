package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class ReactionFavorFactorConfig extends AbstractConfig<Float, Guild> {
    public ReactionFavorFactorConfig() {
        super("reaction_favor_factor", "", ConfigCategory.FAVOR, .1F, "The factor by which favor is bestowed on a guild user for a reaction");
    }
}
