package com.github.kaaz.emily.config.configs.guild;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Playlist;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/2/2017.
 */
public class GuildActivePlaylistConfig extends AbstractConfig<Playlist, Guild> {
    public GuildActivePlaylistConfig() {
        super("guild_active_playlist", BotRole.GUILD_TRUSTEE, Playlist.getPlaylist(Playlist.GLOBAL_PLAYLIST_ID),
                "The active playlist for the guild");
    }
}
