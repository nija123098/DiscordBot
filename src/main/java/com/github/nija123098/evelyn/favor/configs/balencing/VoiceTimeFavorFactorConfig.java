package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class VoiceTimeFavorFactorConfig extends AbstractConfig<Float, Guild> {
    public VoiceTimeFavorFactorConfig() {
        super("voice_favor_factor", BotRole.GUILD_TRUSTEE, 5f, "The factor by which favor is bestowed on a guild user for 5 minutes of voice time");
    }
}
