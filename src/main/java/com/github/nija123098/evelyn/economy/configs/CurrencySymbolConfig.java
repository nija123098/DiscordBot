package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class CurrencySymbolConfig extends AbstractConfig<String, Guild> {
    public CurrencySymbolConfig() {
        super("currency_symbol", "", ConfigCategory.ECONOMY, EmoticonHelper.getChars("cookie", false), "The symbol currency is represented by");
    }

    @Override
    public String setValue(Guild configurable, String value) {
        if (EmoticonHelper.checkUnicode(value)) throw new ArgumentException("not a unicode character");
        return super.setValue(configurable, value);
    }
}
