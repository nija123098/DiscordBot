package com.github.kaaz.emily.audio.commands.current;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class CurrentRepeatCommand extends AbstractCommand {
    public CurrentRepeatCommand() {
        super(CurrentCommand.class, "repeat", null, "loop", null, "Repeats the currently playing song");
    }
    @Command
    public void command(MessageMaker maker, GuildAudioManager manager, @Argument(optional = true) Boolean loop){
        manager.loop(loop == null ? !manager.isLooping() : loop);
        maker.withOK();
    }
}
