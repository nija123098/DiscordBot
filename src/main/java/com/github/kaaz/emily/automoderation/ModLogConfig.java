package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class ModLogConfig extends AbstractConfig<Channel, Guild> {
    public ModLogConfig() {
        super("mod_log", BotRole.GUILD_TRUSTEE, null, "The channel log of moderator actions");
    }
}
