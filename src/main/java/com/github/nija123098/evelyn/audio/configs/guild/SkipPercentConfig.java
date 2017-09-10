package com.github.nija123098.evelyn.audio.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 6/5/2017.
 */
public class SkipPercentConfig extends AbstractConfig<Integer, Guild> {
    public SkipPercentConfig() {
        super("music_vote_percent", BotRole.GUILD_TRUSTEE, 40, "The percent of concerting users required to skip a track");
    }
    @Override
    protected void validateInput(Guild configurable, Integer val) {
        if (val < 1 && val > 100) throw new ArgumentException("Volume value must be between 1 and 100");
    }
}
