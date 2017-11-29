package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class CurrentPMCommand extends AbstractCommand {
    public CurrentPMCommand() {
        super(CurrentCommand.class, "pm", null, null, null, "PMs you the source of the currently playing track");
    }
    @Command
    public void command(Guild guild, MessageMaker maker, Track track){
        CurrentCommand.command(guild, maker.withDM(), track);
    }
}
