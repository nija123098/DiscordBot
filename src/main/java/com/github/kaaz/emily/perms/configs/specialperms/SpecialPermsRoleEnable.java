package com.github.kaaz.emily.perms.configs.specialperms;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 3/25/2017.
 */
public class SpecialPermsRoleEnable extends AbstractConfig<Boolean, Role> {
    public SpecialPermsRoleEnable() {
        super("role_special_perms_enable", BotRole.GUILD_TRUSTEE, false, "");
    }
}
