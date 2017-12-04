package com.github.nija123098.evelyn.fun.starboard;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import static com.github.nija123098.evelyn.config.ConfigCategory.LOGGING;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StarBoardLocationConfig extends AbstractConfig<Channel, Guild> {
    public StarBoardLocationConfig() {
        super("star_board", "Star Board", LOGGING, (Channel) null, "The location of the starboard");
    }
}
