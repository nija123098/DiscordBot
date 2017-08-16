package com.github.kaaz.emily.automoderation.modaction.support;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class ModActionConfig extends AbstractConfig<Map<Integer, AbstractModAction>, Guild> {
    public ModActionConfig() {// todo remove some eventually
        super("mod_action_history", BotRole.GUILD_TRUSTEE, new HashMap<>(0), "The history of mod actions");
    }
}
