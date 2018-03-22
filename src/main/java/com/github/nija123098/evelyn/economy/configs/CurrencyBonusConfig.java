package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class CurrencyBonusConfig extends AbstractConfig<Integer, GlobalConfigurable> {
    public CurrencyBonusConfig() {
        super("currency_bonus", "currency bonus", ConfigCategory.STAT_TRACKING, 0, "current bonus amount");
    }
}
