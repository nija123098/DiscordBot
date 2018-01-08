package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class LanguageLevelViolationFactorConfig extends AbstractConfig<Float, Guild> {
    public LanguageLevelViolationFactorConfig() {
        super("current_money", "language_favor_factor", ConfigCategory.FAVOR, 50F, "The factor by which favor is taken from a guild user for a language violation");
    }
}
