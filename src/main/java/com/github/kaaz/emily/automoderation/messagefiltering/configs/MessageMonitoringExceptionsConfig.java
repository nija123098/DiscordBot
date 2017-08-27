package com.github.kaaz.emily.automoderation.messagefiltering.configs;

import com.github.kaaz.emily.automoderation.messagefiltering.MessageMonitoringLevel;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class MessageMonitoringExceptionsConfig extends AbstractConfig<Set<MessageMonitoringLevel>, Channel> {
    public MessageMonitoringExceptionsConfig() {
        super("language_monitoring_channel_exceptions", BotRole.GUILD_TRUSTEE, new HashSet<>(0), "The channel based exceptions of message monitoring");
    }
}
