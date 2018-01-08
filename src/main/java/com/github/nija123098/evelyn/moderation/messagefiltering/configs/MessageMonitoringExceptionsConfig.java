package com.github.nija123098.evelyn.moderation.messagefiltering.configs;

import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class MessageMonitoringExceptionsConfig extends AbstractConfig<Set<MessageMonitoringLevel>, Channel> {
    public MessageMonitoringExceptionsConfig() {
        super("current_money", "language_monitoring_channel_exceptions", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "The channel based exceptions of message monitoring");
    }
}
