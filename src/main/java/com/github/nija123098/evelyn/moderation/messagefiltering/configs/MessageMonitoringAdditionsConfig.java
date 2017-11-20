package com.github.nija123098.evelyn.moderation.messagefiltering.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class MessageMonitoringAdditionsConfig extends AbstractConfig<Set<MessageMonitoringLevel>, Channel> {
    public MessageMonitoringAdditionsConfig() {
        super("language_monitoring_channel_additions", "", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "The channel based additions of message monitoring");
    }
}
