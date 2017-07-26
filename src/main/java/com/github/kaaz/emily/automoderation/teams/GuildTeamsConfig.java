package com.github.kaaz.emily.automoderation.teams;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 7/23/2017.
 */
public class GuildTeamsConfig extends AbstractConfig<List<String>, Guild> {
    public GuildTeamsConfig() {
        super("guild_teams", BotRole.GUILD_TRUSTEE, new ArrayList<>(0), "The list of teams");
    }
}
