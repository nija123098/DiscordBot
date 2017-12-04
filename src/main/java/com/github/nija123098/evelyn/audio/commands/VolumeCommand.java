package com.github.nija123098.evelyn.audio.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exception.ArgumentException;

import static com.github.nija123098.evelyn.command.ContextType.NONE;
import static com.github.nija123098.evelyn.command.ModuleLevel.MUSIC;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class VolumeCommand extends AbstractCommand {
    public VolumeCommand() {
        super("volume", MUSIC, "vol", null, "Gets or sets the volume of the music");
    }

    @Command// manager for requirement
    public void command(GuildAudioManager manager, Guild guild, @Argument(optional = true, replacement = NONE) Integer value, MessageMaker maker) {
        if (value == null) maker.append(manager.getVolume() + "").appendRaw("%");
        else {
            value = value < 0 ? manager.getVolume() + value : value;
            if (value < 1 || value > 101) throw new ArgumentException("You can't set the volume to " + value);
            manager.setVolume(value);
        }
    }
}
