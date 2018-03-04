package com.github.nija123098.evelyn.moderation.temporary;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordVoiceLeave;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.service.services.ScheduleService;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TemporaryChannelConfig extends AbstractConfig<Long, Channel> {
    static final Set<VoiceChannel> DELETE_ON_NO_USERS = new HashSet<>();
    public TemporaryChannelConfig() {
        super("temporary_channel_time", "", ConfigCategory.GAME_TEMPORARY_CHANNELS, 0L, "This channel will be deleted after a certan amount of time");
        Launcher.registerAsyncStartup(() -> this.getNonDefaultSettings().forEach((channel, aLong) -> {
            if (aLong <= System.currentTimeMillis()) attemptDelete(channel);
            else ScheduleService.schedule(aLong - System.currentTimeMillis(), () -> attemptDelete(channel));
        }));
    }

    @Override
    public Long setValue(Channel configurable, Long value) {
        ScheduleService.schedule(value - System.currentTimeMillis(), () -> attemptDelete(configurable));
        return super.setValue(configurable, value);
    }

    @EventListener
    public void handle(DiscordVoiceLeave update) {
        if (DELETE_ON_NO_USERS.contains(update.getChannel()) && update.getChannel().getConnectedUsers().isEmpty()) attemptDelete(update.getChannel());
    }
    static void attemptDelete(Channel channel) {
        if (channel instanceof VoiceChannel && !((VoiceChannel) channel).getConnectedUsers().isEmpty()) DELETE_ON_NO_USERS.add(((VoiceChannel) channel));
        else {
            try {
                channel.delete();
            } catch (PermissionsException e) {
                // todo
            }
        }
    }
}
