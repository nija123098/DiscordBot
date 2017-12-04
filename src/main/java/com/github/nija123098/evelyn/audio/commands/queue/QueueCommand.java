package com.github.nija123098.evelyn.audio.commands.queue;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.github.nija123098.evelyn.command.ModuleLevel.MUSIC;
import static com.github.nija123098.evelyn.util.FormatHelper.repeat;
import static com.github.nija123098.evelyn.util.Time.getAbbreviated;
import static com.github.nija123098.evelyn.util.Time.getAbbreviatedMusic;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class QueueCommand extends AbstractCommand {
    public QueueCommand() {
        super("queue", MUSIC, "q", null, "Shows the music queue");
    }

    @Command
    public void command(GuildAudioManager manager, MessageMaker maker) {
        List<Track> tracks = manager.getQueue();
        if (tracks.size() == 0) {
            maker.append("There are no items in the queue");
            return;
        }
        AtomicLong time = new AtomicLong();
        boolean stream = false;
        Long trackTime;
        for (Track track : tracks) {
            trackTime = track.getLength();
            if (trackTime == null) {
                stream = true;
                break;
            } else time.addAndGet(trackTime);
        }
        maker.append("There are " + tracks.size() + " items.\n");
        if (!stream) maker.append("Total estimated play time of ").appendRaw(getAbbreviated(time.get()));
        tracks.forEach(track -> maker.getNewListPart().append((track.getLength() == null ? "stream " : buffer(getAbbreviatedMusic(track.getLength())) + " | " + track.getName())));
    }

    private static String buffer(String s) {
        int i = s.length() - 7;
        if (i > 0) s = repeat(' ', i) + s;
        return s;
    }
}
