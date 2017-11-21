package com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/8/2017.
 */
public class VolumeConfig extends AbstractConfig<Integer, Guild> {
    public VolumeConfig() {
        super("music_volume", "", BotRole.USER, ConfigCategory.MUSIC, 30, "The volume the bot speaks and plays music at.");
    }
    @Override
    protected Integer validateInput(Guild configurable, Integer val) {
        if (val < 1 && val > 100) throw new ArgumentException("Volume value must be between 1 and 100");
        return val;
    }
}
