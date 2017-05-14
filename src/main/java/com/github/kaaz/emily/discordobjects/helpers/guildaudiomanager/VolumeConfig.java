package com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/8/2017.
 */
public class VolumeConfig extends AbstractConfig<Integer, Guild> {
    public VolumeConfig() {
        super("music_volume", BotRole.USER, 40, "The volume the bot speaks and plays music at.");
    }
    @Override
    protected void validateInput(Guild configurable, Integer val) {
        if (val < 1 && val > 100) throw new ArgumentException("Volume value must be between 1 and 100");
    }
}
