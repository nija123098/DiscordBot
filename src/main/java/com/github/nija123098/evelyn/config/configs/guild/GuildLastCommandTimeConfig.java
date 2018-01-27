package com.github.nija123098.evelyn.config.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildLastCommandTimeConfig extends AbstractConfig<Long, Guild> {
    private static final AtomicReference<GuildLastCommandTimeConfig> CONFIG = new AtomicReference<>();
    public GuildLastCommandTimeConfig() {
        super("guild_last_command_time", "", ConfigCategory.STAT_TRACKING, -1L, "The last time the user used a command");
        CONFIG.set(this);
    }
    public static void update(Guild guild){
        CONFIG.get().setValue(guild, System.currentTimeMillis());
    }

    public static long get(Guild guild){
        return CONFIG.get().getValue(guild);
    }

}
