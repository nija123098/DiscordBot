package com.github.kaaz.emily.config.configs.user;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 2/22/2017.
 */
public class RoleConfig extends AbstractConfig<String> {
    public RoleConfig() {
        super("bot_role", BotRole.BOT_OWNER, "none", "The role the user has with the bot");
    }
}
