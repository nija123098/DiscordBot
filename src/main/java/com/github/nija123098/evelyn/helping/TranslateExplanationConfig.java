package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

public class TranslateExplanationConfig extends AbstractConfig<Boolean, User> {
    public TranslateExplanationConfig() {
        super("current_money", "translate_explanation", ConfigCategory.STAT_TRACKING, false, "If the bot has explained the translate command");
    }
}
