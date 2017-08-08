package com.github.kaaz.emily.information.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.DiscordDataReload;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordPresenceUpdate;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.service.services.ScheduleService;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class UptimeConfig extends AbstractConfig<Long, User> {
    private static final Set<User> TO_UPDATE = new HashSet<>();
    static {
        ScheduleService.scheduleRepeat(60_000, 10000, () -> {
            Set<User> users = new HashSet<>(TO_UPDATE);
            TO_UPDATE.removeAll(users);// I know that I can use synchronization here
            users.forEach(user -> ConfigHandler.setSetting(UptimeConfig.class, user, System.currentTimeMillis()));
        });
    }
    public UptimeConfig() {
        super("up_time", BotRole.BOT_ADMIN, "How long a user has been on or off line", user -> {
            TO_UPDATE.add(user);
            return null;
        });
    }
    @EventListener
    public void handle(DiscordPresenceUpdate event){
        long current = System.currentTimeMillis();
        this.changeSetting(event.getUser(), previous -> {
            ConfigHandler.alterSetting(UptimeStatsConfig.class, event.getUser(), statusLongMap -> statusLongMap.compute(event.getOldPresence().getStatus(), (status, aLong) -> (aLong == null ? 0 : aLong) + (previous == null ? current : current - previous)));
            return current;
        });
    }
    @EventListener
    public void handle(DiscordDataReload reload){
        ConfigHandler.getTypeInstances(User.class).forEach(this::getValue);
    }
}
