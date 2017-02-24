package com.github.kaaz.discordbot.config.configs.user;

import com.github.kaaz.discordbot.config.AbstractConfig;
import com.github.kaaz.discordbot.perms.BotRole;

/**
 * Made by nija123098 on 2/22/2017.
 */
public class FavorConfig extends AbstractConfig {
    public FavorConfig() {
        super("favor_amount", BotRole.BOT_ADMIN, "0", "How much the bot likes you");
    }
}
