package com.github.kaaz.emily.automoderation.messagefiltering.configs;

import com.github.kaaz.emily.automoderation.messagefiltering.MessageMonitoringLevel;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

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
            MessageMonitoringLevel.EXCESSIVE_CAPITALS, MessageMonitoringLevel.EXCESSIVE_PINGING,
            MessageMonitoringLevel.SPAM));
    public MessageMonitoringConfig() {
        super("language_monitoring", BotRole.GUILD_TRUSTEE, DEFAULT, "Language monitoring for all channels unless if exceptions are given");
    }
}
