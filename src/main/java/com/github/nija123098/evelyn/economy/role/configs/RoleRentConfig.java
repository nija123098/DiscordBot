package com.github.nija123098.evelyn.economy.role.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RoleRentConfig extends AbstractConfig<Integer, Role> {
    public RoleRentConfig() {
        super("role_subscription_cost", "", ConfigCategory.ECONOMY, (Integer) null, "The amount of currency that gaining a role for an hour costs");
    }
}
