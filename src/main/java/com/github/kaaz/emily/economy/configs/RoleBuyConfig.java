package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/5/2017.
 */
public class RoleBuyConfig extends AbstractConfig<Integer, Role> {
    public RoleBuyConfig() {
        super("role_buy", BotRole.GUILD_TRUSTEE, null, "The map for allowing buying roles with guild based currency");
    }
}
