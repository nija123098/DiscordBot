package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class CurrentRepeatCommand extends AbstractCommand {
    public CurrentRepeatCommand() {
        super(CurrentCommand.class, "repeat", null, "loop", "loop", "Repeats the currently playing song");
    }
    @Command
    public void command(GuildAudioManager manager, @Argument(optional = true, replacement = ContextType.NONE) Boolean loop){
        manager.loop(loop == null ? !manager.isLooping() : loop);
    }
}
