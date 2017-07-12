package com.github.kaaz.emily.information.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.DiscordDataReload;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordPresenceUpdate;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class UptimeConfig extends AbstractConfig<Long, User> {
    public UptimeConfig() {
        super("up_time", BotRole.BOT_ADMIN, "How long a user has been on or off line", user -> System.currentTimeMillis());
    }
    @EventListener
    public void handle(DiscordPresenceUpdate event){
        long current = System.currentTimeMillis();
        this.changeSetting(event.getUser(), previous -> {
            ConfigHandler.alterSetting(UptimeStatsConfig.class, event.getUser(), statusLongMap -> statusLongMap.compute(event.getOldPresence().getStatus(), (status, aLong) -> {
                if (aLong == null) aLong = 0L;
                return aLong + (current - previous);
            }));
            return current;
        });
    }
    @EventListener
    public void handle(DiscordDataReload reload){
        ConfigHandler.getTypeInstances(User.class).stream().filter(user -> this.getValue(user) == null).forEach(user -> this.setValue(user, System.currentTimeMillis()));
    }
}
