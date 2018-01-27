package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.Configurable;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrencySymbolConfig extends AbstractConfig<String, Configurable> {
    public CurrencySymbolConfig() {
        super("currency_symbol", "Currency Symbol", ConfigCategory.ECONOMY, "🍪", "The symbol currency is represented by");
    }
}
