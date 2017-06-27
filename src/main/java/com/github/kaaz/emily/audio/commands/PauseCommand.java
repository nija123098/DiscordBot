package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class PauseCommand extends AbstractCommand {
    public PauseCommand() {
        super("pause", ModuleLevel.MUSIC, "unpause", null, "Pauses the currently playing track");
    }
    @Command
    public void command(GuildAudioManager manager){
        manager.pause(!manager.isPaused());
    }
}
