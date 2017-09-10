package com.github.nija123098.evelyn.audio.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class PauseCommand extends AbstractCommand {
    public PauseCommand() {
        super("pause", ModuleLevel.MUSIC, "unpause", "pause_button", "Pauses the currently playing track");
    }
    @Command
    public void command(GuildAudioManager manager){
        manager.pause(!manager.isPaused());
    }
}
