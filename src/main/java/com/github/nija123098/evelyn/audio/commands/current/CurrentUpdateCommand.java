package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.util.ThreadHelper;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.nija123098.evelyn.audio.commands.current.CurrentCommand.getPlayBar;
import static com.github.nija123098.evelyn.util.EmoticonHelper.getChars;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrentUpdateCommand extends AbstractCommand {
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Current-Update-Thread"));
    private static final Map<Integer, ScheduledFuture<?>> MAP = new ConcurrentHashMap<>();
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();
    public CurrentUpdateCommand() {
        super(CurrentCommand.class, "update", null, null, null, "Updates a now playing message every ten seconds");
    }

    @Command
    public void command(GuildAudioManager manager, MessageMaker maker) {
        Integer key = ATOMIC_INTEGER.incrementAndGet();
        MAP.put(key, SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            Track track;
            if ((track = manager.currentTrack()) != null) {
                Long length = track.getLength();
                maker.forceCompile().appendRaw(getChars("notes", true) + track.getName() + (length != null ? "\n" + getPlayBar(manager.isPaused(), manager.currentTime(), length) : ""));
            } else MAP.remove(key).cancel(false);
        }, 0, 10, TimeUnit.SECONDS));
    }
}
