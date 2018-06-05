package com.github.nija123098.evelyn.information.subsription;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SubscriptionsConfig extends AbstractConfig<Set<SubscriptionLevel>, Channel> {
    public SubscriptionsConfig() {
        super("channel_subscriptions", "", ConfigCategory.STAT_TRACKING, new HashSet<>(), "The subscriptions a channel has");
    }
}
