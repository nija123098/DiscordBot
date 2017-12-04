package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GameTimeFavorFactorConfig extends AbstractConfig<Float, Guild> {
    public GameTimeFavorFactorConfig() {
        super("linked_game_favor_factor", "Game Favor Factor", ConfigCategory.FAVOR, 2f, "The factor by which favor is bestowed on a guild user for 5 minutes of linked game time");
    }
}
