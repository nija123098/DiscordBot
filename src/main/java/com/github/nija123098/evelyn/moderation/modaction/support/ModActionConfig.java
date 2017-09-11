package com.github.nija123098.evelyn.moderation.modaction.support;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

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
