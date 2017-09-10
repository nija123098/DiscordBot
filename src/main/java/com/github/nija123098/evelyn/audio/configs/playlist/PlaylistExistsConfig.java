package com.github.nija123098.evelyn.audio.configs.playlist;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PlaylistExistsConfig extends AbstractConfig<Boolean, Playlist> {
    public PlaylistExistsConfig() {
        super("playlist_exists", BotRole.BOT_ADMIN, false, "If a playlist exists");
    }
}
