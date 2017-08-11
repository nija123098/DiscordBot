package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

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
