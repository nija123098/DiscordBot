package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.util.LangString;

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
