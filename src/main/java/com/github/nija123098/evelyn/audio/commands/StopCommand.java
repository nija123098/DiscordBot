package com.github.nija123098.evelyn.audio.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import static com.github.nija123098.evelyn.command.ModuleLevel.MUSIC;
import static com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager.getManager;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StopCommand extends AbstractCommand {
    public StopCommand() {
        super("stop", MUSIC, "leave", null, "Makes the bot stop playing music and leave the channel");
    }

    @Command
    public void command(Guild guild, MessageMaker maker) {
        GuildAudioManager manager = getManager(guild);
        if (manager == null) maker.append("But I'm not even in a channel with you").mustEmbed();
        else {
            maker.append("Stopping Track.").mustEmbed();
            manager.leave();
        }
    }
}
