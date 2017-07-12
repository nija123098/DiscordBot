package com.github.kaaz.emily.information.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Presence;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 7/7/2017.
 */
public class UptimeStatsConfig extends AbstractConfig<Map<Presence.Status, Long>, User> {
    public UptimeStatsConfig() {
        super("uptime_downtime", BotRole.BOT_ADMIN, new HashMap<>(), "The uptime of a user in millis");
    }
}
