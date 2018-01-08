package com.github.nija123098.evelyn.fun.starboard;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class StarLevelRequirementConfig extends AbstractConfig<Map<StarLevel, Integer>, Guild> {
    private static final Map<StarLevel, Integer> DEFAULT = new HashMap<>(StarLevel.values().length);
    static {
        DEFAULT.put(StarLevel.SPARKLE, 3);
        DEFAULT.put(StarLevel.DWARF, 5);
        DEFAULT.put(StarLevel.YELLOW, 7);
        DEFAULT.put(StarLevel.SOLAR, 10);
        DEFAULT.put(StarLevel.EXPLOSIVE, 15);
    }
    public StarLevelRequirementConfig() {
        super("star_level_requirement", ConfigCategory.LOGGING, DEFAULT, "The minimum requirements for a certain level");
    }
}
