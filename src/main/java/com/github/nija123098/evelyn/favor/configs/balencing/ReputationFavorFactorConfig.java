package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class ReputationFavorFactorConfig extends AbstractConfig<Float, Guild> {
    public ReputationFavorFactorConfig() {
        super("reputation_favor_factor", "", ConfigCategory.FAVOR, 30f, "The factor by which favor is bestowed on a guild user for a reaction");
    }
}
