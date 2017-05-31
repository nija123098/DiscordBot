package com.github.kaaz.emily.audio.commands.current;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Track;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class CurrentPMCommand extends AbstractCommand {
    public CurrentPMCommand(Class<? extends AbstractCommand> superCommand, String name, String absoluteAliases, String emoticonAliases, String relativeAliases, String help) {
        super(CurrentCommand.class, "pm", null, null, null, "PMs you the source of the currently playing track");
    }
    @Command
    public void command(Track track, MessageMaker maker){
        maker.withDM().appendRaw(track.getSource());
    }
}
