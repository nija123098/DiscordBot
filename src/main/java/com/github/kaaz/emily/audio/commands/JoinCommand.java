package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.VoiceChannel;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class JoinCommand extends AbstractCommand {
    public JoinCommand() {
        super("join", ModuleLevel.MUSIC, null, null, "Makes the bot join the voice channel you are in");
    }
    @Command
    public static void command(GuildAudioManager manager, VoiceChannel channel, MessageMaker maker){
        if (!manager.voiceChannel().equals(channel)) maker.append("Sorry, I am already in another channel");
    }
}
