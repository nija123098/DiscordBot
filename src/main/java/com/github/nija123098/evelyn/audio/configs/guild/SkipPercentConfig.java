package com.github.nija123098.evelyn.audio.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exception.ArgumentException;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SkipPercentConfig extends AbstractConfig<Integer, Guild> {
    public SkipPercentConfig() {
        super("music_vote_percent", "Skip Vote Percent", ConfigCategory.MUSIC, 40, "The percent of concerting users required to skip a track");
    }
    @Override
    protected Integer validateInput(Guild configurable, Integer val) {
        if (val < 1 && val > 100) throw new ArgumentException("Volume value must be between 1 and 100");
        return val;
    }
}
