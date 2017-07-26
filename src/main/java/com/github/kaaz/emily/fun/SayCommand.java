package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.util.LangString;

/**
 * Made by nija123098 on 7/23/2017.
 */
public class SayCommand extends AbstractCommand {
    public SayCommand() {
        super("say", ModuleLevel.FUN, null, null, "Makes the bot say something");
    }
    @Command
    public void command(@Argument(info = "The stuff you want me to say") String s, Channel channel, MessageMaker maker){
        GuildAudioManager manager = GuildAudioManager.getManager(channel.getGuild());
        if (manager != null) manager.queueSpeech(new LangString(false, s));// && BotRole.CONTRIBUTOR.hasRequiredRole(user, channel.getGuild())
        else maker.appendRaw(s);
    }
}
