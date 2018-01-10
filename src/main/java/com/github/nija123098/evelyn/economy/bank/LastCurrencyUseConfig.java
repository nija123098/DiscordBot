package com.github.nija123098.evelyn.economy.bank;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class LastCurrencyUseConfig extends AbstractConfig<String, User> {
    public LastCurrencyUseConfig() {
        super("last_currency_use", "", ConfigCategory.STAT_TRACKING, "", "The last time the bank command was used");
    }

    @Override
    public String setValue(User configurable, String value, boolean overrideCache) {
        return super.setValue(configurable, value, true);
    }

    @Override
    public String getValue(User configurable, boolean overrideCache) {
        return super.getValue(configurable, true);
    }
}