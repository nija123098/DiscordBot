package com.github.nija123098.evelyn.moderation.modaction.support;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.moderation.modaction.MuteModActionCommand;
import com.github.nija123098.evelyn.util.ThreadHelper;
import javafx.util.Pair;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MuteActionConfig extends AbstractConfig<Pair<Long, Set<Role>>, GuildUser> {
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Mute-Action-Config-Thread"));
    public MuteActionConfig() {
        super("temp_bans", "", ConfigCategory.STAT_TRACKING, (Pair<Long, Set<Role>>) null, "The temp bans and time they are unbanned");
        Launcher.registerPostStartup(() -> ConfigHandler.getNonDefaultSettings(MuteActionConfig.class).forEach((guildUser, pair) -> EXECUTOR_SERVICE.schedule(() -> {
            if (!(pair.getKey() - 10_000 < System.currentTimeMillis())) return;
            MuteModActionCommand.unmute(guildUser.getGuild(), guildUser.getUser());
        }, pair.getKey(), TimeUnit.MILLISECONDS)));
    }
}
