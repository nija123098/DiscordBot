package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class GlobalNicknameConfig extends AbstractConfig<String, GlobalConfigurable> {
    public GlobalNicknameConfig() {
        super("global_nickname", BotRole.BOT_ADMIN, null, "The global nickname");
    }
}
