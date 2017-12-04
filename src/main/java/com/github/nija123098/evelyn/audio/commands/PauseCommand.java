package com.github.nija123098.evelyn.audio.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

import static com.github.nija123098.evelyn.command.ModuleLevel.MUSIC;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PauseCommand extends AbstractCommand {
    public PauseCommand() {
        super("pause", MUSIC, "unpause", "pause_button", "Pauses the currently playing track");
    }

    @Command
    public void command(GuildAudioManager manager) {
        manager.pause(!manager.isPaused());
    }

    @Override
    public boolean useReactions() {
        return true;
    }
}
