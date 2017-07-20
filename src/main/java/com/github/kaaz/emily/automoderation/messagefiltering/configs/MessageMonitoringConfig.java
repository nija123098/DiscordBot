package com.github.kaaz.emily.automoderation.messagefiltering.configs;

import com.github.kaaz.emily.automoderation.messagefiltering.MessageMonitoringType;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class MessageMonitoringConfig extends AbstractConfig<Set<MessageMonitoringType>, Guild> {
    private static final Set<MessageMonitoringType> DEFAULT = new HashSet<>(Arrays.asList(
            MessageMonitoringType.FAKE_DANGER, MessageMonitoringType.SLURS,
            MessageMonitoringType.WHITE_SPACE, MessageMonitoringType.INVOCATIONS,
            MessageMonitoringType.EXCESSIVE_CAPITALS, MessageMonitoringType.EXCESSIVE_PINGING));
    public MessageMonitoringConfig() {
        super("language_monitoring", BotRole.GUILD_TRUSTEE, DEFAULT, "Language monitoring for all channels unless if exceptions are given");
    }
}
