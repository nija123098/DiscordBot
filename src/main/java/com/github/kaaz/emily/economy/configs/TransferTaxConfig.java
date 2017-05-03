package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/3/2017.
 */
public class TransferTaxConfig extends AbstractConfig<Float, Configurable> {
    public TransferTaxConfig() {
        super("transfer_tax", BotRole.BOT_ADMIN, 0F, "The tax made by the governor for a transfer.");
    }
}
