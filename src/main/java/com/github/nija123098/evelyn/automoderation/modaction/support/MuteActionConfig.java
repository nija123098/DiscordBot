package com.github.nija123098.evelyn.automoderation.modaction.support;

import com.github.nija123098.evelyn.automoderation.modaction.MuteModActionCommand;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.services.ScheduleService;
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
