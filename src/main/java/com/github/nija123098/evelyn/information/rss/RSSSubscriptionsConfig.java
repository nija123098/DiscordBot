package com.github.nija123098.evelyn.information.rss;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RSSSubscriptionsConfig extends AbstractConfig<List<String>, Channel> {
    public RSSSubscriptionsConfig() {
        super("rss_subscriptions", "", ConfigCategory.STAT_TRACKING, new ArrayList<>(), "The RSS feeds a channel is subscribed to.");
    }
}
