package com.github.kaaz.emily.config.configs.playlist;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Playlist;
import com.github.kaaz.emily.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PlaylistContentsConfig extends AbstractConfig<List<String>, Playlist> {
    public PlaylistContentsConfig() {
        super("playlist_contents", BotRole.BOT_ADMIN, new ArrayList<>(), "The contents of the playlist");
    }
}
