package com.github.nija123098.evelyn.moderation.messagefiltering.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class MessageMonitoringConfig extends AbstractConfig<Set<MessageMonitoringLevel>, Guild> {
    public MessageMonitoringConfig() {
        super("language_monitoring", "", ConfigCategory.MODERATION, new HashSet<>(), "Language monitoring for all channels unless if exceptions are given");
    }
}
