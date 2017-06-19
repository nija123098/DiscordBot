package com.github.kaaz.emily.perms.configs.specialperms;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/13/2017.
 */
public class GuildSpecialPermsConfig extends AbstractConfig<SpecialPermsContainer, Guild> {
    public GuildSpecialPermsConfig() {
        super("guild_special_perms", BotRole.GUILD_TRUSTEE, null, "Changes the permissions on Emily commands");
    }
}
