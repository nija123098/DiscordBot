package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.automoderation.languagefiltering.LanguageLevel;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class LanguageFilteringLevelConfig extends AbstractConfig<LanguageLevel, Guild> {
    public LanguageFilteringLevelConfig() {//                    disabled
        super("language_filtering_level", BotRole.GUILD_TRUSTEE, LanguageLevel.NONE, "The level of poor language allowed on a server");
    }
}
