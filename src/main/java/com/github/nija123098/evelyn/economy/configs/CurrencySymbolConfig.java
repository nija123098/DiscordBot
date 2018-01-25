package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrencySymbolConfig extends AbstractConfig<String, Configurable> {
    public CurrencySymbolConfig() {
        super("currency_symbol", "Currency Symbol", ConfigCategory.ECONOMY, "", "The symbol currency is represented by");
    }

    @Override
    public String setValue(Configurable configurable, String value) {
        if (EmoticonHelper.getChars(value.toLowerCase(), false) != null) value = EmoticonHelper.getChars(value, false);
        if (EmoticonHelper.getName(value) == null) throw new ArgumentException("Unknown emote. Please choose a valid Unicode emote.");
        return super.setValue(configurable, EmoticonHelper.getName(value));
    }

    @Override
    public String getValue(Configurable configurable) {
        return EmoticonHelper.getChars(super.getValue(configurable), false);
    }
}
