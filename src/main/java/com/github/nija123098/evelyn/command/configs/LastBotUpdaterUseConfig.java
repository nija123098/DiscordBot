package com.github.nija123098.evelyn.command.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class LastBotUpdaterUseConfig extends AbstractConfig<Long, GlobalConfigurable>{
    public LastBotUpdaterUseConfig() {
        super("last_bot_updater_use", "", ConfigCategory.STAT_TRACKING, 0L, "tracks the last successful run of update command");
    }
}
