package com.github.nija123098.evelyn.audio.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class QueueTrackOnlyConfig extends AbstractConfig<Boolean, Guild> {
    public QueueTrackOnlyConfig() {
        super("queue_only", "Queue Only", ConfigCategory.MUSIC, false, "If no playlist should play after the queue is empty");
    }
}
