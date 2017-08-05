package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/16/2017.
 */
public class RoleRentConfig extends AbstractConfig<Integer, Role> {
    public RoleRentConfig() {
        super("role_subscription_cost", BotRole.GUILD_TRUSTEE, null, "The amount of currency that gaining a role for an hour costs");
    }
}
