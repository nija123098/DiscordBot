package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;

/**
 * Made by nija123098 on 5/16/2017.
 */
public class RoleRentConfig extends AbstractConfig<Integer, Role> {
    public RoleRentConfig() {
        super("role_subscription_cost", "", ConfigCategory.ECONOMY, (Integer) null, "The amount of currency that gaining a role for an hour costs");
    }
}
