package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class StopCommand extends AbstractCommand {
    public StopCommand() {
        super("stop", ModuleLevel.MUSIC, "leave", null, "Makes the bot stop playing music and leave the channel");
    }
    @Command
    public void command(GuildAudioManager manager){
        manager.leave();
    }
}
