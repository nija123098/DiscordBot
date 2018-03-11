package com.github.nija123098.evelyn.audio.commands.queue;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class QueueRemoveCommand extends AbstractCommand {
    public QueueRemoveCommand() {
        super(QueueCommand.class, "remove", null, null, null, "Removes a specific track from the queue");
    }
    @Command
    public void command(@Argument Track track, GuildAudioManager manager, MessageMaker maker) {
        if (manager.getQueue().remove(track)) maker.withOK();
        else maker.append("The queue didn't contain that track!");
    }
}
