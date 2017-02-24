package com.github.kaaz.discordbot.config.configs.user;

import com.github.kaaz.discordbot.config.AbstractMultiConfig;
import com.github.kaaz.discordbot.perms.BotRole;

import java.util.Collections;

/**
 * Made by nija123098 on 2/22/2017.
 */
public class GlobalBotRoleFlagConfig extends AbstractMultiConfig {
    public GlobalBotRoleFlagConfig() {
        super("global_flag_ranks", BotRole.BOT_OWNER, Collections.singletonList("none"), "The config for if an user is a contributor to the bot");
    }
}
