package com.github.kaaz.emily.perms.configs.specialperms;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 3/25/2017.
 */
public class PermsModuleBlacklistConfig extends AbstractConfig<List<String>, Role>{
    public PermsModuleBlacklistConfig() {
        super("special_perms_module_blacklist", BotRole.GUILD_TRUSTEE, new ArrayList<>(0), "");
    }
}
