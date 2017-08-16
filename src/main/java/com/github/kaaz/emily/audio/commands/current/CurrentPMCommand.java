package com.github.kaaz.emily.audio.commands.current;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class CurrentPMCommand extends AbstractCommand {
    public CurrentPMCommand() {
        super(CurrentCommand.class, "pm", null, null, null, "PMs you the source of the currently playing track");
    }
    @Command
    public void command(Guild guild, MessageMaker maker){
        CurrentCommand.command(guild, maker.withDM());
    }
}
