package com.github.nija123098.evelyn.moderation.linkedgames;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildLinkedGamesConfig extends AbstractConfig<Set<String>, Guild> {
    public GuildLinkedGamesConfig() {
        super("linked_games", "", ConfigCategory.STAT_TRACKING, new HashSet<>(), "The games linked to the server");
    }
}
