package com.github.nija123098.evelyn.audio.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class StopAfterCommand extends AbstractCommand {
    public StopAfterCommand() {
        super(StopCommand.class, "afternp", null, null, "after", "Makes the bot stop playing music and leave the channel after the current track is over");
    }
    @Command
    public void command(GuildAudioManager manager, VoiceChannel channel, MessageMaker maker, User user){
        if (GuildAudioManager.validListeners(channel) > 1 && !BotRole.GUILD_TRUSTEE.hasRequiredRole(user, channel.getGuild())){
            maker.append("You can't make that decision for everyone!");
        } else manager.leaveAfterThis();
    }
}
