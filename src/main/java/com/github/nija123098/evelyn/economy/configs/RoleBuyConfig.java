package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/5/2017.
 */
public class RoleBuyConfig extends AbstractConfig<Integer, Role> {
    public RoleBuyConfig() {
        super("role_buy", BotRole.GUILD_TRUSTEE, null, "The map for allowing buying roles with guild based currency");
    }
}
