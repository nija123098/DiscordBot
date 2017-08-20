package com.github.kaaz.emily.automoderation.modaction.support;

import com.github.kaaz.emily.automoderation.modaction.MuteModActionCommand;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.launcher.Launcher;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.ThreadProvider;
import javafx.util.Pair;

import java.util.Set;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class MuteActionConfig extends AbstractConfig<Pair<Long, Set<Role>>, GuildUser> {
    public MuteActionConfig() {
        super("temp_bans", BotRole.GUILD_TRUSTEE, null, "The temp bans and time they are unbanned");
        Launcher.registerAsyncStartup(() -> ConfigHandler.getNonDefaultSettings(MuteActionConfig.class).forEach((guildUser, pair) -> ScheduleService.schedule(pair.getKey(), () -> {
            if (!(pair.getKey() - 10_000 < System.currentTimeMillis())) return;
            MuteModActionCommand.unmute(guildUser.getGuild(), guildUser.getUser());
        })));
    }
}
