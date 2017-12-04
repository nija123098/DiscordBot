package com.github.nija123098.evelyn.config.configs.guild;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import static com.github.nija123098.evelyn.audio.GlobalPlaylist.GLOBAL_PLAYLIST;
import static com.github.nija123098.evelyn.config.ConfigCategory.STAT_TRACKING;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildActivePlaylistConfig extends AbstractConfig<Playlist, Guild> {
    public GuildActivePlaylistConfig() {
        super("guild_active_playlist", "", STAT_TRACKING, GLOBAL_PLAYLIST,
                "The active playlist for the guild");
    }
}
