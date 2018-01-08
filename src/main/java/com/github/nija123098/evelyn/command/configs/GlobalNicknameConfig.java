package com.github.nija123098.evelyn.command.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class GlobalNicknameConfig extends AbstractConfig<String, GlobalConfigurable> {
    public GlobalNicknameConfig() {
        super("global_nickname", ConfigCategory.STAT_TRACKING, (String) null, "The global nickname");
    }
}
