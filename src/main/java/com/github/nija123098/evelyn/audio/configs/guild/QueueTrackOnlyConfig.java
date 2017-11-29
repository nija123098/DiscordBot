package com.github.nija123098.evelyn.audio.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 5/4/2017.
 */
public class QueueTrackOnlyConfig extends AbstractConfig<Boolean, Guild> {
    public QueueTrackOnlyConfig() {
        super("queue_only", "Queue Only", ConfigCategory.MUSIC, false, "If no playlist should play after the queue is empty");
    }
}
