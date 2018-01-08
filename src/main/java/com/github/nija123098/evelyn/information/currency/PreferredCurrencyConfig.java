package com.github.nija123098.evelyn.information.currency;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

public class PreferredCurrencyConfig extends AbstractConfig<Currency, User> {
    public PreferredCurrencyConfig() {
        super("preferred_currency", ConfigCategory.ECONOMY, Currency.USD, "The currency to display info for currency commands");
    }
}
