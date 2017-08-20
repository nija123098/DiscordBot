package com.github.kaaz.emily.automoderation.modaction.support;

import com.github.kaaz.emily.automoderation.modaction.TempBanModActionCommand;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.launcher.Launcher;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.ThreadProvider;

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
