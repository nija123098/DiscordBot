package com.github.kaaz.emily.config.configs.guild;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 3/27/2017.
 */
public class GuildModuleEnableConfig extends AbstractConfig<Set<String>, Set<String>, Guild>{
    public GuildModuleEnableConfig() {
        super("guild_module_enable", BotRole.GUILD_TRUSTEE, new HashSet<>(Arrays.asList("MUSIC", "FUN", "BOT_ADMINISTRATIVE", "ADMINISTRATIVE", "ECONOMY", "NONE")), // todo optimize
                "The config to enable or disable modules of the bot.");
    }
}
