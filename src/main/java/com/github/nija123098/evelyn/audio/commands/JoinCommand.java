package com.github.nija123098.evelyn.audio.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;

import static com.github.nija123098.evelyn.command.ModuleLevel.MUSIC;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class JoinCommand extends AbstractCommand {
    public JoinCommand() {
        super("join", MUSIC, null, null, "Makes the bot join the voice channel you are in");
    }

    @Command
    public static void command(GuildAudioManager manager, VoiceChannel channel, MessageMaker maker) {
        if (!manager.voiceChannel().equals(channel)) maker.append("Sorry, I am already in another channel");
    }
}
