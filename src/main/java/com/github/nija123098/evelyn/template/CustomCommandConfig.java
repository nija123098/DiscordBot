package com.github.nija123098.evelyn.template;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CustomCommandConfig extends AbstractConfig<List<CustomCommand>, Guild> {
    public CustomCommandConfig() {
        super("custom_commands", "", ConfigCategory.STAT_TRACKING, new ArrayList<>(0), "The map of custom command by name then functions");
    }
}
