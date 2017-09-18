package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class MuteRoleConfig extends AbstractConfig<Role, Guild> {
    public MuteRoleConfig() {
        super("mute_role", ConfigCategory.MODERATION, (Role) null, "The role members who are muted will be assigned");
    }
}
