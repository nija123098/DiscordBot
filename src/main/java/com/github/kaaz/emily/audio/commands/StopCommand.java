package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class StopCommand extends AbstractCommand {
    public StopCommand() {
        super("stop", ModuleLevel.MUSIC, "leave", null, "Makes the bot stop playing music and leave the channel");
    }
    @Command
    public void command(Guild guild, MessageMaker maker){
        GuildAudioManager manager = GuildAudioManager.getManager(guild);
        if (manager == null) maker.append("But I'm not even in a channel with you");
        else manager.leave();
    }
}
