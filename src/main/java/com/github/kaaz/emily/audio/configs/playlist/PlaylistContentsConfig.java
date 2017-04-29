package com.github.kaaz.emily.audio.configs.playlist;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.audio.Playlist;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PlaylistContentsConfig extends AbstractConfig<List<Track>, Playlist> {
    public PlaylistContentsConfig() {
        super("playlist_contents", BotRole.BOT_ADMIN, new ArrayList<>(), "The contents of the playlist");
    }
}
