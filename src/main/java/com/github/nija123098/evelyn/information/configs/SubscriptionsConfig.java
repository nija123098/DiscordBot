package com.github.nija123098.evelyn.information.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.information.subsription.SubscriptionLevel;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 5/23/2017.
 */
public class SubscriptionsConfig extends AbstractConfig<Set<SubscriptionLevel>, Channel> {
    public SubscriptionsConfig() {
        super("channel_subscriptions", BotRole.GUILD_TRUSTEE, new HashSet<>(), "The subscriptions a channel has");
    }
}
