package com.github.nija123098.evelyn.moderation.modaction.support;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class StrikeActionConfig extends AbstractConfig<Integer, GuildUser> {
    public StrikeActionConfig() {
        super("discipline_strike", BotRole.GUILD_TRUSTEE, 0, "The count of strikes a guild user has in a guild");
    }
}
