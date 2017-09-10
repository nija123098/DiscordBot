package com.github.nija123098.evelyn.automoderation.messagefiltering.configs;

import com.github.nija123098.evelyn.automoderation.messagefiltering.MessageMonitoringLevel;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class MessageMonitoringConfig extends AbstractConfig<Set<MessageMonitoringLevel>, Guild> {
    private static final Set<MessageMonitoringLevel> DEFAULT = new HashSet<>(Arrays.asList(
            MessageMonitoringLevel.FAKE_DANGER, MessageMonitoringLevel.SLURS,
            MessageMonitoringLevel.WHITE_SPACE, MessageMonitoringLevel.INVOCATIONS,
            MessageMonitoringLevel.EXCESSIVE_CAPITALS, MessageMonitoringLevel.SPAM));
    public MessageMonitoringConfig() {
        super("language_monitoring", BotRole.GUILD_TRUSTEE, DEFAULT, "Language monitoring for all channels unless if exceptions are given");
    }
}
