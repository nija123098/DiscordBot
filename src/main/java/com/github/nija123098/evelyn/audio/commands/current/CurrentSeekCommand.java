package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.ContextException;
import com.github.nija123098.evelyn.util.Time;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrentSeekCommand extends AbstractCommand {
    public CurrentSeekCommand() {
        super(CurrentCommand.class, "seek", null, null, "goto", "Go to specified timestamp of track (eg 3m10s");
    }

    @Command
    public void command(@Argument Time time, GuildAudioManager manager) {
        Track track = manager.currentTrack();
        Long length = track.getLength();
        if (length == null) throw new ContextException("You can't seek on this track");
        if (length < time.schedualed()) throw new ArgumentException("The track is not that long");
        manager.seek(time.timeUntil());
    }
}
