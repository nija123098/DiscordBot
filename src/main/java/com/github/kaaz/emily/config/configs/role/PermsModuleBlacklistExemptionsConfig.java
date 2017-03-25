package com.github.kaaz.emily.config.configs.role;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 3/25/2017.
 */
public class PermsModuleBlacklistExemptionsConfig extends AbstractConfig<List<String>, List<String>, Role>{
    public PermsModuleBlacklistExemptionsConfig() {
        super("special_perms_module_blacklist_exemptions", BotRole.GUILD_TRUSTEE, new ArrayList<>(0), "");
    }
}
