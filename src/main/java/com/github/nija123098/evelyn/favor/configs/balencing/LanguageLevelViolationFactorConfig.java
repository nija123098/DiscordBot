package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class LanguageLevelViolationFactorConfig extends AbstractConfig<Float, Guild> {
    public LanguageLevelViolationFactorConfig() {
        super("language_favor_factor", BotRole.GUILD_TRUSTEE, 50F, "The factor by which favor is taken from a guild user for a language violation");
    }
}
