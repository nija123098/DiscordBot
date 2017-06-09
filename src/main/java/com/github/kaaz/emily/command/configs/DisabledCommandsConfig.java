package com.github.kaaz.emily.command.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class DisabledCommandsConfig extends AbstractConfig<Set<String>, GlobalConfigurable> {
    public DisabledCommandsConfig() {
        super("disabled_commands", BotRole.BOT_ADMIN, new HashSet<>(), "The globally disabled commands.");
    }
}
