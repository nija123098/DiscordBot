package com.github.nija123098.evelyn.fun.starboard;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import java.util.HashMap;
import java.util.Map;

import static com.github.nija123098.evelyn.config.ConfigCategory.LOGGING;
import static com.github.nija123098.evelyn.fun.starboard.StarLevel.*;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StarLevelRequirementConfig extends AbstractConfig<Map<StarLevel, Integer>, Guild> {
    private static final Map<StarLevel, Integer> DEFAULT = new HashMap<>(StarLevel.values().length);

    static {
        DEFAULT.put(SPARKLE, 3);
        DEFAULT.put(DWARF, 5);
        DEFAULT.put(YELLOW, 7);
        DEFAULT.put(SOLAR, 10);
        DEFAULT.put(EXPLOSIVE, 15);
    }

    public StarLevelRequirementConfig() {
        super("star_level_requirement", "", LOGGING, DEFAULT, "The minimum requirements for a certain level");
    }
}
