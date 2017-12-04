package com.github.nija123098.evelyn.economy.role.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RoleBuyConfig extends AbstractConfig<Integer, Role> {
    public RoleBuyConfig() {
        super("role_buy", "", ConfigCategory.ECONOMY, (Integer) null, "The map for allowing buying roles with guild based currency");
    }
}
