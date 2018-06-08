package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildLeave;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserLeave;
import com.github.nija123098.evelyn.favor.FavorChangeEvent;
import com.github.nija123098.evelyn.favor.FavorHandler;
import com.github.nija123098.evelyn.launcher.Launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TimeFavorFactorConfig extends AbstractConfig<Float, Guild> {
    private static final ArrayList<GuildUser> DEPENDENTS = new ArrayList<>();
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
    private static final Map<GuildUser, Future<?>> FUTURE_MAP = new HashMap<>();
    private static final AtomicReference<ScheduledFuture> SCHEDULE_FUTURE = new AtomicReference<>();
    private static final AtomicInteger CURRENT_INDEX = new AtomicInteger();
    public TimeFavorFactorConfig() {
        super("time_favor_factor", "Time Favor Factor", ConfigCategory.FAVOR, 0F, "The time per 15min a user has been in the server without leaving");// .007F
        Launcher.registerAsyncStartup(() -> {
            int sizeRequired = this.getNonDefaultSettings().keySet().stream().map(Guild::getUserSize).reduce((guild, guild2) -> guild + guild2).get();
            DEPENDENTS.ensureCapacity(sizeRequired + 10);
            this.getNonDefaultSettings().forEach((guild, val) -> guild.getUsers().stream().map(user -> GuildUser.getGuildUser(guild, user)).forEach(DEPENDENTS::add));
            calculateFuture();
        });
    }
    private static void calculateFuture() {
        if (DEPENDENTS.size() == 0) return;
        if (SCHEDULE_FUTURE.get() != null) SCHEDULE_FUTURE.get().cancel(false);
        SCHEDULE_FUTURE.set(EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            int index = CURRENT_INDEX.getAndIncrement();
            if (DEPENDENTS.size() < index) {
                CURRENT_INDEX.set(0);
                index = 0;
            }
            GuildUser guildUser = DEPENDENTS.get(index);
            float amount = FavorHandler.getFavorAmount(guildUser);
            EventDistributor.distribute(new FavorChangeEvent(guildUser, amount, amount - ConfigHandler.getSetting(TimeFavorFactorConfig.class, guildUser.getGuild())));
        }, 120_000, 1_200_000 / DEPENDENTS.size(), TimeUnit.MILLISECONDS));
    }//             20 minutes

    @Override
    public Float setValue(Guild configurable, Float value) {
        Float old = this.getValue(configurable);
        Float val = super.setValue(configurable, value);
        if (old != 0 && val != 0) return val;
        if (val != 0) registerAll(configurable);
        else registerAll(configurable);
        return val;
    }

    public void registerAll(Guild guild) {
        if (guild.getUserSize() > 10) calculateFuture();
        List<User> users = guild.getUsers();
        DEPENDENTS.ensureCapacity(users.size());
        users.stream().map(user -> GuildUser.getGuildUser(guild, user)).forEach(DEPENDENTS::add);
    }
    public void handle(DiscordGuildJoin join) {
        if (this.getValue(join.getGuild()) == 0) return;
        registerAll(join.getGuild());
    }
    public void handle(DiscordGuildLeave leave) {
        if (this.getValue(leave.getGuild()) != 0) return;
        leave.getGuild().getUsers().forEach(user -> DEPENDENTS.remove(GuildUser.getGuildUser(leave.getGuild(), user)));
    }
    public void handle(DiscordUserJoin join) {
        if (this.getValue(join.getGuild()) == 0) return;
        DEPENDENTS.add(GuildUser.getGuildUser(join.getGuild(), join.getUser()));
    }
    public void handle(DiscordUserLeave leave) {
        if (this.getValue(leave.getGuild()) != 0) return;
        DEPENDENTS.remove(GuildUser.getGuildUser(leave.getGuild(), leave.getUser()));
    }
}
