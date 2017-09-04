package com.github.kaaz.emily.fun.starboard;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class StarLevelRequirementConfig extends AbstractConfig<Map<StarLevel, Integer>, Guild> {
    private static final Map<StarLevel, Integer> DEFAULT = new HashMap<>(StarLevel.values().length);
    static {
        DEFAULT.put(StarLevel.SPARKLE, 1);
        DEFAULT.put(StarLevel.DWARF, 4);
        DEFAULT.put(StarLevel.YELLOW, 8);
        DEFAULT.put(StarLevel.SOLAR, 14);
        DEFAULT.put(StarLevel.EXPLOSIVE, 25);
    }
    public StarLevelRequirementConfig() {
        super("star_level_requirement", BotRole.GUILD_TRUSTEE, DEFAULT, "The minimum requirements for a certain level");
    }
}
