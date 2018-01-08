package com.github.nija123098.evelyn.information.descriptions;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;

/**
 * @author nija123098
 */
public class GuildUserDescriptionConfig extends AbstractConfig<String, GuildUser> {
    public GuildUserDescriptionConfig() {
        super("current_money", "guild_user_description", ConfigCategory.STAT_TRACKING, (String) null, "Information about the user that overrides per server");
    }
}
