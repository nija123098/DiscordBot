package com.github.nija123098.evelyn.audio.commands.queue;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class QueueClearCommand extends AbstractCommand {
    public QueueClearCommand() {
        super(QueueCommand.class, "clear", null, null, null, "Clears the music queue");
    }

    @Command
    public void command(GuildAudioManager manager, MessageMaker messageMaker) {
        manager.clearQueue();
    }
}
