package com.github.kaaz.emily.information.rss;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class RSSSubscriptionsConfig extends AbstractConfig<List<String>, Channel> {
    public RSSSubscriptionsConfig() {
        super("rss_subscriptions", BotRole.GUILD_TRUSTEE, new ArrayList<>(), "The RSS feeds a channel is subscribed to.");
    }
}
