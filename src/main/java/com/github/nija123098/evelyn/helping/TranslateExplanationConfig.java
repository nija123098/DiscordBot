package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

public class TranslateExplanationConfig extends AbstractConfig<Boolean, User> {
    public TranslateExplanationConfig() {
        super("translate_explanation", BotRole.SYSTEM, false, "If the bot has explained the translate command");
    }
}
