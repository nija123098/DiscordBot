package com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.discordobjects.wrappers.VoiceChannel;
import com.github.kaaz.emily.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 5/3/2017.
 */
public class PlayQueueConfig extends AbstractConfig<List<Track>, VoiceChannel> {
    public PlayQueueConfig() {
        super("play_queue", BotRole.SYSTEM, new ArrayList<>(), "The play Q for shutdown situations.");
    }
}
