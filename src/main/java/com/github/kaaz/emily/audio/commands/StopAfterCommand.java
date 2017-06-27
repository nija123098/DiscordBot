package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.VoiceChannel;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class StopAfterCommand extends AbstractCommand {
    public StopAfterCommand() {
        super(StopCommand.class, "afternp", null, null, "after", "Makes the bot stop playing music and leave the channel after the current track is over");
    }
    @Command
    public void command(GuildAudioManager manager, VoiceChannel channel, MessageMaker maker){
        if (channel.getConnectedUsers().size() != 1){
            maker.append("You can't make that decision for everyone!");
            return;
        }
        manager.leaveAfterThis();
    }
}
