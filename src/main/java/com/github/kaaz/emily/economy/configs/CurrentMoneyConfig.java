package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class CurrentMoneyConfig extends AbstractConfig<Float, Configurable> {
    public CurrentMoneyConfig() {
        super("current_money", BotRole.BOT_ADMIN, 0F, "The amount of money a guild user has");
    }

    @Override
    public Float setValue(Configurable configurable, Float value) {
        return super.setValue(configurable, value);
    }
}
