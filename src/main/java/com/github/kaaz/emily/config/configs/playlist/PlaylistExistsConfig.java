package com.github.kaaz.emily.config.configs.playlist;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Playlist;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PlaylistExistsConfig extends AbstractConfig<Boolean, Playlist> {
    public PlaylistExistsConfig() {
        super("playlist_exists", BotRole.BOT_ADMIN, false, "If a playlist exists");
    }
}
