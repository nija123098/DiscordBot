package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.services.ScheduleService;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 5/16/2017.
 */
public class RoleSubscriptionsConfig extends AbstractConfig<Map<Role, Long>, GuildUser> {
    public RoleSubscriptionsConfig() {
        super("role_subscriptions", BotRole.GUILD_TRUSTEE, new HashMap<>(), "The subscriptions for subscribed roles for a guild user.");
        Launcher.registerAsyncStartup(() -> {
            long current = System.currentTimeMillis();
            this.getNonDefaultSettings().forEach((guildUser, map) -> map.forEach((role, expiration) -> scheduleRemoval(expiration - current, guildUser, role)));
        });
    }
    public static void scheduleRemoval(long delay, GuildUser guildUser, Role role){
        ScheduleService.schedule(delay, () -> {
            if (ConfigHandler.getSetting(RoleSubscriptionsConfig.class, guildUser).get(role) > System.currentTimeMillis() + 10) return;
            guildUser.getUser().removeRole(role);
        });
    }
}
