package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class MuteRoleConfig extends AbstractConfig<Role, Guild> {
    public MuteRoleConfig() {
        super("mute_role", BotRole.GUILD_TRUSTEE, null, "The role members who are muted will be assigned");
    }
}
