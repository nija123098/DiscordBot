package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.util.LangString;

/**
 * Made by nija123098 on 7/23/2017.
 */
public class SayCommand extends AbstractCommand {
    public SayCommand() {
        super("say", ModuleLevel.FUN, null, null, "Makes the bot say something");
    }
    @Command
    public void command(@Argument(info = "Text you want me to repeat, if you're in a voice channel you'll hear it there") String s, Channel channel, @Context(softFail = true) Guild guild, MessageMaker maker){
        GuildAudioManager manager = GuildAudioManager.getManager(guild);
        if (manager != null) manager.queueSpeech(new LangString(false, s));// && BotRole.CONTRIBUTOR.hasRequiredRole(user, channel.getGuild())
        else maker.appendRaw(s);
    }
}
