package com.github.kaaz.emily.information.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.information.subsription.SubscriptionLevel;
import com.github.kaaz.emily.perms.BotRole;

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
