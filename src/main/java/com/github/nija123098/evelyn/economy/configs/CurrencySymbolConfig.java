package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class CurrencySymbolConfig extends AbstractConfig<String, Guild> {
    public CurrencySymbolConfig() {
        super("currency_symbol", "", ConfigCategory.ECONOMY, EmoticonHelper.getChars("cookie", true), "The symbol currency is represented by");
    }
}
