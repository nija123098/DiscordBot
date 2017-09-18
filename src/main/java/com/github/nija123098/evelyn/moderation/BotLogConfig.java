package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

public class BotLogConfig extends AbstractConfig<Channel, Guild> {
    public BotLogConfig() {
        super("bot_log", ConfigCategory.LOGGING, (Channel) null, "Where the bot prints usages of meaningful commands to");
    }
}
