package com.github.kaaz.emily.favor.configs.balencing;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class LanguageLevelViolationFactorConfig extends AbstractConfig<Float, Guild> {
    public LanguageLevelViolationFactorConfig() {
        super("language_violation_favor_factor", BotRole.GUILD_TRUSTEE, 5F, "The factor by which favor is taken from a guild user for a language violation");
    }
}
