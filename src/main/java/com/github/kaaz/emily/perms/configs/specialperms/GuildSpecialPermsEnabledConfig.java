package com.github.kaaz.emily.perms.configs.specialperms;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 3/25/2017.
 */
public class GuildSpecialPermsEnabledConfig extends AbstractConfig<Boolean, Guild>{
    public GuildSpecialPermsEnabledConfig() {
        super("guild_special_perms", BotRole.GUILD_TRUSTEE, false,
                "The config the enable special role based command whitelisting and blacklisting for the guild.  " +
                        "Roles that you would like to be effected will still need their role_special_perms enabled.");
    }
}
