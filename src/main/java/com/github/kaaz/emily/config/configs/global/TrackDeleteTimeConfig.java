package com.github.kaaz.emily.config.configs.global;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 3/28/2017.
 */
public class TrackDeleteTimeConfig extends AbstractConfig<Long, Configurable.GlobalConfigurable>{
    public TrackDeleteTimeConfig() {
        super("track_delete_time", BotRole.BOT_ADMIN, 432000000L,
                "The time it takes for a song to not be used enough to be considered for file deletion");
    }
}
