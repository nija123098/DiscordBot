package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserLeave;
import com.github.nija123098.evelyn.favor.FavorChangeEvent;
import com.github.nija123098.evelyn.favor.FavorHandler;
import com.github.nija123098.evelyn.launcher.Launcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TimeFavorFactorConfig extends AbstractConfig<Float, Guild> {
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
    private static final Map<GuildUser, ScheduledFuture<?>> FUTURE_MAP = new HashMap<>();
    public TimeFavorFactorConfig() {
        super("time_favor_factor", "Time Favor Factor", ConfigCategory.FAVOR, 0F, "The time per 15min a user has been in the server without leaving");// .007F
        Launcher.registerAsyncStartup(() -> this.getNonDefaultSettings().forEach((guild, val) -> guild.getUsers().stream().map(user -> GuildUser.getGuildUser(guild, user)).forEach(this::registerDependent)));
    }

    @Override
    public Float setValue(Guild configurable, Float value) {
        Float val = super.setValue(configurable, value);
        if (val != 0) configurable.getUsers().stream().map(user -> GuildUser.getGuildUser(configurable, user)).forEach(this::registerDependent);
        else configurable.getUsers().stream().map(user -> GuildUser.getGuildUser(configurable, user)).map(FUTURE_MAP::remove).forEach(scheduledFuture -> scheduledFuture.cancel(true));
        return val;
    }

    public void handle(DiscordUserJoin join) {
        registerDependent(GuildUser.getGuildUser(join.getGuild(), join.getUser()));
    }

    public void handle(DiscordUserLeave leave) {
        FUTURE_MAP.remove(GuildUser.getGuildUser(leave.getGuild(), leave.getUser())).cancel(true);
    }

    private void registerDependent(GuildUser guildUser) {
        if (FUTURE_MAP.containsKey(guildUser)) return;
        FUTURE_MAP.put(guildUser, EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            float amount = FavorHandler.getFavorAmount(guildUser);
            EventDistributor.distribute(new FavorChangeEvent(guildUser, amount, amount - this.getValue(guildUser.getGuild())));
        }, 0, 15, TimeUnit.MINUTES));
    }
}
