package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Time;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Made by nija123098 on 5/24/2017.
 */
public class QueueCommand extends AbstractCommand {
    public QueueCommand() {
        super("queue", ModuleLevel.MUSIC, "q", null, "Shows the music queue");
    }
    @Command
    public void command(GuildAudioManager manager, MessageMaker maker){
        List<Track> tracks = manager.getQueue();
        if (tracks.size() == 0){
            maker.append("There are no items in the queue");
            return;
        }
        AtomicLong time = new AtomicLong();
        boolean stream = false;
        Long trackTime;
        for (Track track : tracks){
            trackTime = track.getLength();
            if (trackTime == null) {
                stream = true;
                break;
            } else time.addAndGet(trackTime);
        }
        maker.append("There are " + tracks.size() + " items.\n");
        if (!stream) maker.append("Total estimated play time of ").appendRaw(Time.getAbbreviated(time.get()));
        tracks.forEach(track -> maker.getNewListPart().append((track.getLength() == null ? "stream " : buffer(Time.getAbbreviatedMusic(track.getLength())) + " | " + track.getName())));
    }
    private static String buffer(String s){
        int i = s.length() - 7;
        if (i > 0) s = FormatHelper.repeat(' ', i) + s;
        return s;
    }
}
