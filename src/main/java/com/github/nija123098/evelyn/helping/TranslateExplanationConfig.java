package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TranslateExplanationConfig extends AbstractConfig<Boolean, User> {
    public TranslateExplanationConfig() {
        super("translate_explanation", "", ConfigCategory.STAT_TRACKING, false, "If the bot has explained the translate command");
    }
}
