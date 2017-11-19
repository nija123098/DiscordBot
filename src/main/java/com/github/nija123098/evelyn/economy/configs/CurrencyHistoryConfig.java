package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.economy.CurrencyTransfer;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class CurrencyHistoryConfig extends AbstractConfig<List<CurrencyTransfer>, Configurable> {
    public CurrencyHistoryConfig() {
        super("currency_history", ConfigCategory.STAT_TRACKING, new ArrayList<>(0), "The history of money");
    }
}
