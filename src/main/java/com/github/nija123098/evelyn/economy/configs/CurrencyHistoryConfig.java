package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.economy.currencytransfer.CurrencyTransfer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrencyHistoryConfig extends AbstractConfig<List<CurrencyTransfer>, Configurable> {
    public CurrencyHistoryConfig() {
        super("currency_history", "", ConfigCategory.STAT_TRACKING, new ArrayList<>(0), "The history of money");
    }
}
