package com.github.kaaz.emily.config.configs.track;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.perms.BotRole;

import java.io.File;

/**
 * Made by nija123098 on 3/28/2017.
 */
public class TrackFileConfig extends AbstractConfig<File, Track> {
    public TrackFileConfig() {
        super("track_file_config", BotRole.BOT_OWNER, null, "The config to point at the location of the music file");
    }
}
