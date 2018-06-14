package com.github.nija123098.evelyn.moderation.modaction.support;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.moderation.modaction.TempBanModActionCommand;
import com.github.nija123098.evelyn.util.ThreadHelper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TempBanActionConfig extends AbstractConfig<Long, GuildUser> {
    public static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Temp-Ban-Pardon-Thread"));
    public TempBanActionConfig() {
        super("temp_ban", "", ConfigCategory.STAT_TRACKING, (Long) null, "The time they are unbanned");
        Launcher.registerPostStartup(() -> ConfigHandler.getNonDefaultSettings(TempBanActionConfig.class).forEach((guildUser, val) -> {
            if (val < System.currentTimeMillis()) TempBanModActionCommand.unban(guildUser.getGuild(), guildUser.getUser());
            EXECUTOR_SERVICE.schedule(() -> TempBanModActionCommand.unban(guildUser.getGuild(), guildUser.getUser()), val - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }));
    }
}
