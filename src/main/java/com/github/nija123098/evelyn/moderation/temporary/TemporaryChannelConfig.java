package com.github.nija123098.evelyn.moderation.temporary;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordVoiceLeave;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.util.ThreadHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TemporaryChannelConfig extends AbstractConfig<Long, Channel> {
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Temporary-Channel-Thread"));
    private static final Set<VoiceChannel> DELETE_ON_NO_USERS = new HashSet<>();
    public TemporaryChannelConfig() {
        super("temporary_channel_time", "", ConfigCategory.GAME_TEMPORARY_CHANNELS, 0L, "This channel will be deleted after a certan amount of time");
        Launcher.registerPostStartup(() -> this.getNonDefaultSettings().forEach((channel, aLong) -> {
            if (aLong <= System.currentTimeMillis()) attemptDelete(channel);
            else EXECUTOR_SERVICE.schedule(() -> attemptDelete(channel), aLong - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }));
    }

    @Override
    public Long setValue(Channel configurable, Long value) {
        EXECUTOR_SERVICE.schedule(() -> attemptDelete(configurable), value - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
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
