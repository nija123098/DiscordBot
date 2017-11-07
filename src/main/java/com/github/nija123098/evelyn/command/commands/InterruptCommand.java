package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.util.LangString;

/**
 * Made by nija123098 on 7/16/2017.
 */
public class InterruptCommand extends AbstractCommand {
    public InterruptCommand() {
        super("int", ModuleLevel.FUN, null, null, "Interrups");
    }
    @Command
    public void command(GuildAudioManager manager){
        manager.interrupt(new LangString(false, "I am interrupting"));
    }
}
