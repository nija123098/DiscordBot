package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

import static com.github.nija123098.evelyn.command.ContextType.NONE;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrentRepeatCommand extends AbstractCommand {
    public CurrentRepeatCommand() {
        super(CurrentCommand.class, "repeat", null, "loop", "loop", "Repeats the currently playing song");
    }

    @Command
    public void command(GuildAudioManager manager, @Argument(optional = true, replacement = NONE) Boolean loop) {
        manager.loop(loop == null ? !manager.isLooping() : loop);
    }

    @Override
    public boolean useReactions() {
        return true;
    }
}
