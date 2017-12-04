package com.github.nija123098.evelyn.moderation.modaction.support;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ModActionConfig extends AbstractConfig<Map<Integer, AbstractModAction>, Guild> {
    public ModActionConfig() {// todo remove some eventually
        super("mod_action_history", "", ConfigCategory.STAT_TRACKING, new HashMap<>(0), "The history of mod actions");
    }
}
