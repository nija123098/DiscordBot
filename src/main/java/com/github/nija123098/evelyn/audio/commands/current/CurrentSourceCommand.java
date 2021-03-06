package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrentSourceCommand extends AbstractCommand {
    public CurrentSourceCommand() {
        super(CurrentCommand.class, "source", null, null, null, "Shows the source of the video");
    }

    @Command
    public void command(Track track, MessageMaker maker) {
        maker.append(track.getSource());
    }
}
