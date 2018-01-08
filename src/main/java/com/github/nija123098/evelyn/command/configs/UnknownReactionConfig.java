package com.github.nija123098.evelyn.command.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.function.Function;

public class UnknownReactionConfig extends AbstractConfig<Boolean, Guild> {
    public UnknownReactionConfig() {
        super("unknown_command_response", ConfigCategory.GUILD_PERSONALIZATION, guild -> guild.getUsers().stream().filter(User::isBot).count() > 5, "If the bot ignores unknown commands");
    }
}
