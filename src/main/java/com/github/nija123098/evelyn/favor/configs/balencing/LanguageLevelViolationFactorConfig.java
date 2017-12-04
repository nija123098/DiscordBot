package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class LanguageLevelViolationFactorConfig extends AbstractConfig<Float, Guild> {
    public LanguageLevelViolationFactorConfig() {
        super("language_favor_factor", "Language Favor Factor", ConfigCategory.FAVOR, 50F, "The factor by which favor is taken from a guild user for a language violation");
    }
}
