package com.github.nija123098.evelyn.moderation.modaction.support;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

public class WarningLogConfig extends AbstractConfig<List<String>, GuildUser>{
    public WarningLogConfig() {
        super("warning_log", "", BotRole.SYSTEM, ConfigCategory.LOGGING, new ArrayList<>(0), "The list of warnings for a server user");
    }
}
