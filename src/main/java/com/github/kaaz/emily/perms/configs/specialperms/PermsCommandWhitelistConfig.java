package com.github.kaaz.emily.perms.configs.specialperms;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 3/25/2017.
 */
public class PermsCommandWhitelistConfig extends AbstractConfig<List<String>, Role>{
    public PermsCommandWhitelistConfig() {
        super("special_perms_command_whitelist", BotRole.GUILD_TRUSTEE, new ArrayList<>(0), "");
    }
}
