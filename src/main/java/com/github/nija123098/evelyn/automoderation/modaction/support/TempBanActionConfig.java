package com.github.nija123098.evelyn.automoderation.modaction.support;

import com.github.nija123098.evelyn.automoderation.modaction.TempBanModActionCommand;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.services.ScheduleService;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class TempBanActionConfig extends AbstractConfig<Long, GuildUser> {
    public TempBanActionConfig() {
        super("temp_ban", BotRole.GUILD_TRUSTEE, null, "The time they are unbanned");
        Launcher.registerAsyncStartup(() -> ConfigHandler.getNonDefaultSettings(TempBanActionConfig.class).forEach((guildUser, val) -> ScheduleService.schedule(val, () -> {
            if (!(val > System.currentTimeMillis())) return;
            TempBanModActionCommand.unban(guildUser.getGuild(), guildUser.getUser());
        })));
    }
}