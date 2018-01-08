package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.ContextException;
import com.github.nija123098.evelyn.util.Time;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class CurrentSeekCommand extends AbstractCommand {
    public CurrentSeekCommand() {
        super(CurrentCommand.class, "seek", null, null, "goto", "Go to specified timestamp of track (eg 3m10s");
    }
    @Command
    public void command(@Argument Time time, GuildAudioManager manager){
        Track track = manager.currentTrack();
        Long length = track.getLength();
        if (length == null) throw new ContextException("You can't seek on this track");
        if (length < time.schedualed()) throw new ArgumentException("The track is not that long");
        manager.seek(time.timeUntil());
    }
}
