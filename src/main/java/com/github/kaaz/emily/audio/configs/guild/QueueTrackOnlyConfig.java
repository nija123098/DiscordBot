package com.github.kaaz.emily.audio.configs.guild;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/4/2017.
 */
public class QueueTrackOnlyConfig extends AbstractConfig<Boolean, Guild> {
    public QueueTrackOnlyConfig() {
        super("queue_only", BotRole.GUILD_TRUSTEE, true, "If the playlist should play after the queue is empty");
    }
}
