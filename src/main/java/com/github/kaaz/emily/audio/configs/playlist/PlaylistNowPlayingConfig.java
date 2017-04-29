package com.github.kaaz.emily.audio.configs.playlist;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.audio.Playlist;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class PlaylistNowPlayingConfig extends AbstractConfig<Integer, Playlist> {
    public PlaylistNowPlayingConfig() {
        super("playlist_now_playing", BotRole.BOT_ADMIN, null, "The current playing track id");
    }
}
