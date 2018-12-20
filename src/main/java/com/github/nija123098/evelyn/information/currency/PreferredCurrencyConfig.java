package com.github.nija123098.evelyn.information.currency;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PreferredCurrencyConfig extends AbstractConfig<Currency, User> {
    public PreferredCurrencyConfig() {
        super("preferred_currency", "", ConfigCategory.ECONOMY, Currency.USD, "The currency to display info for currency commands");
    }
}
