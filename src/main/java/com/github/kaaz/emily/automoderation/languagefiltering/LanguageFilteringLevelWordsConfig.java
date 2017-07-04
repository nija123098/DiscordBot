package com.github.kaaz.emily.automoderation.languagefiltering;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class LanguageFilteringLevelWordsConfig extends AbstractConfig<Map<LanguageLevel, Set<String>>, GlobalConfigurable> {
    public LanguageFilteringLevelWordsConfig() {
        super("language_filtering_level_words", BotRole.BOT_ADMIN, new HashMap<>(), "The words for language filtering");
    }
}
