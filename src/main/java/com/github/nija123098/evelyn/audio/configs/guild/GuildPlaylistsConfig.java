package com.github.nija123098.evelyn.audio.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import java.util.HashSet;
import java.util.Set;

public class GuildPlaylistsConfig extends AbstractConfig<Set<String>, Guild> {
    public GuildPlaylistsConfig() {
        super("guild_playlists", "", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "The playlists a guild has");
    }
}
