package com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 5/3/2017.
 */
public class PlayQueueConfig extends AbstractConfig<List<Track>, VoiceChannel> {
    public PlayQueueConfig() {
        super("play_queue", ConfigCategory.STAT_TRACKING, new ArrayList<>(0), "The play Q for shutdown situations.");
    }
}
