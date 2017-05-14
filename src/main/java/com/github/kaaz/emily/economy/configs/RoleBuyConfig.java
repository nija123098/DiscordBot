package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 5/5/2017.
 */
public class RoleBuyConfig extends AbstractConfig<Map<Role, Float>, Guild> {
    public RoleBuyConfig() {
        super("role_buy_config", BotRole.GUILD_TRUSTEE, new HashMap<>(), "The map for allowing buying roles with guild based currency");
    }
}
