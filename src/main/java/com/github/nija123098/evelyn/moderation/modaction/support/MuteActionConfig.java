package com.github.nija123098.evelyn.moderation.modaction.support;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.moderation.modaction.MuteModActionCommand;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import javafx.util.Pair;

import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MuteActionConfig extends AbstractConfig<Pair<Long, Set<Role>>, GuildUser> {
    public MuteActionConfig() {
        super("temp_bans", "", ConfigCategory.STAT_TRACKING, (Pair<Long, Set<Role>>) null, "The temp bans and time they are unbanned");
        Launcher.registerAsyncStartup(() -> ConfigHandler.getNonDefaultSettings(MuteActionConfig.class).forEach((guildUser, pair) -> ScheduleService.schedule(pair.getKey(), () -> {
            if (!(pair.getKey() - 10_000 < System.currentTimeMillis())) return;
            MuteModActionCommand.unmute(guildUser.getGuild(), guildUser.getUser());
        })));
    }
}
