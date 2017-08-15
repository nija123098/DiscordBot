package com.github.kaaz.emily.template;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 8/11/2017.
 */
public class CustomCommandConfig extends AbstractConfig<List<CustomCommand>, Guild> {
    public CustomCommandConfig() {
        super("custom_commands", BotRole.GUILD_TRUSTEE, "The map of custom command by name then functions", guild -> new ArrayList<>(0));
    }
}
