package com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * The play queue for storing songs the bot should play after it shuts down.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class PlayQueueConfig extends AbstractConfig<List<Track>, VoiceChannel> {
    public PlayQueueConfig() {
        super("play_queue", ConfigCategory.STAT_TRACKING, new ArrayList<>(0), "The play Q for shutdown situations.");
    }
}
