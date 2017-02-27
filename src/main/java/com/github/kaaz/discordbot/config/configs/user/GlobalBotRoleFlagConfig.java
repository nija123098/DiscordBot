package com.github.kaaz.discordbot.config.configs.user;

import com.github.kaaz.discordbot.config.AbstractConfig;
import com.github.kaaz.discordbot.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 2/22/2017.
 */
public class GlobalBotRoleFlagConfig extends AbstractConfig<List<String>> {
    public GlobalBotRoleFlagConfig() {
        super("global_flag_ranks", BotRole.BOT_OWNER, new ArrayList<>(2), "The config for if an user is a contributor to the bot");
    }
}
