package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class GlobalNicknameConfig extends AbstractConfig<String, GlobalConfigurable> {
    public GlobalNicknameConfig() {
        super("global_nickname", BotRole.BOT_ADMIN, null, "The global nickname");
    }
}
