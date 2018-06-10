package com.github.nija123098.evelyn.moderation.messagefiltering.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.moderation.messagefiltering.filters.URLFilter;

import java.util.HashSet;
import java.util.Set;

public class MessageMonitoringUrlBlacklist extends AbstractConfig<Set<String>, Guild> {
    public MessageMonitoringUrlBlacklist() {
        super("message_monitoring_url_blacklist", "", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "The list of whitelisted urls for the URL message monitor option");
        URLFilter.BLACKLIST = this;
    }
}
