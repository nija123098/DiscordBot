package com.github.nija123098.evelyn.economy.bank;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class LastCurrencyUseConfig extends AbstractConfig<Long, User> {
    public LastCurrencyUseConfig() {
        super("last_currency_use", "", ConfigCategory.STAT_TRACKING, 0L, "The last time the bank command was used");
    }
}