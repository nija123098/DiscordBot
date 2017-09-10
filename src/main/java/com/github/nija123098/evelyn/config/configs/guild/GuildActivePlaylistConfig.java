package com.github.nija123098.evelyn.config.configs.guild;

import com.github.nija123098.evelyn.audio.GlobalPlaylist;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 4/2/2017.
 */
public class GuildActivePlaylistConfig extends AbstractConfig<Playlist, Guild> {
    public GuildActivePlaylistConfig() {
        super("guild_active_playlist", BotRole.GUILD_TRUSTEE, GlobalPlaylist.GLOBAL_PLAYLIST,
                "The active playlist for the guild");
    }
}
