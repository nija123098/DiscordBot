package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrencyNameConfig extends AbstractConfig<String, Guild> {
    public CurrencyNameConfig() {
        super("currency_name", "", ConfigCategory.STAT_TRACKING, "cookies", "The name of the guild currency");
    }
}
