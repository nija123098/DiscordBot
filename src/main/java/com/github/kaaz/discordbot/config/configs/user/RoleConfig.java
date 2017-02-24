package com.github.kaaz.discordbot.config.configs.user;

import com.github.kaaz.discordbot.config.AbstractConfig;
import com.github.kaaz.discordbot.perms.BotRole;

/**
 * Made by nija123098 on 2/22/2017.
 */
public class RoleConfig extends AbstractConfig {
    public RoleConfig() {
        super("bot_role", BotRole.BOT_OWNER, "none", "The role the user has with the bot");
    }
}
