package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.util.ThreadHelper;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.nija123098.evelyn.audio.commands.current.CurrentCommand.getPlayBar;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrentUpdateTitleCommand extends AbstractCommand {
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Current-Update-Thread"));
    private static final Map<Integer, ScheduledFuture<?>> MAP = new ConcurrentHashMap<>();
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();
    public CurrentUpdateTitleCommand() {
        super(CurrentCommand.class, "updatetitle", null, null, null, "updates the topic of the music channel every 10 seconds");
    }

    @Command
    public void command(GuildAudioManager manager, Channel channel) {
        Integer key = ATOMIC_INTEGER.incrementAndGet();
        MAP.put(key, SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            Track track;
            if ((track = manager.currentTrack()) != null) {
                Long length = track.getLength();
                if (length == null) channel.changeTopic("Stream: " + track.getName());
                else channel.changeTopic(getPlayBar(manager.isPaused(), manager.currentTime(), length) + " " + manager.currentTrack().getName());
            }
        }, 0, 10, TimeUnit.SECONDS));
    }
}
