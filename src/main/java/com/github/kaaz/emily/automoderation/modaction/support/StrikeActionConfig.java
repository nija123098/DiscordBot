package com.github.kaaz.emily.automoderation.modaction.support;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class StrikeActionConfig extends AbstractConfig<Integer, GuildUser> {
    public StrikeActionConfig() {
        super("discipline_strike", BotRole.GUILD_TRUSTEE, 0, "The count of strikes a guild user has in a guild");
    }
}
