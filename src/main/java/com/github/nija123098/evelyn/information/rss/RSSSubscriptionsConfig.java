package com.github.nija123098.evelyn.information.rss;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class RSSSubscriptionsConfig extends AbstractConfig<List<String>, Channel> {
    public RSSSubscriptionsConfig() {
        super("rss_subscriptions", ConfigCategory.STAT_TRACKING, new ArrayList<>(), "The RSS feeds a channel is subscribed to.");
    }
}
