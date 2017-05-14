package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class StopAfterCommand extends AbstractCommand {
    public StopAfterCommand() {
        super(StopCommand.class, "afternp", null, null, "after", "Makes the bot stop playing music and leave the channel after the current track is over");
    }
    @Command
    public void command(GuildAudioManager manager){
        manager.leaveAfterThis();
    }
}
