package com.github.nija123098.evelyn.audio.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

/**
 * Made by nija123098 on 5/24/2017.
 */
public class QueueClearCommand extends AbstractCommand {
    public QueueClearCommand() {
        super(QueueCommand.class, "clear", null, null, null, "Clears the music queue");
    }
    @Command
    public void command(GuildAudioManager manager){
        manager.clearQueue();
    }
}
