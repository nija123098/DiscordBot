package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.util.LangString;

import static com.github.nija123098.evelyn.command.ModuleLevel.FUN;
import static com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager.getManager;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SayCommand extends AbstractCommand {
    public SayCommand() {
        super("say", FUN, null, null, "Makes the bot say something");
    }

    @Command
    public void command(@Argument(info = "Text you want me to repeat, if you're in a voice channel you'll hear it there") String s, @Context(softFail = true) Guild guild, MessageMaker maker) {
        GuildAudioManager manager = getManager(guild);
        if (manager != null)
            manager.queueSpeech(new LangString(false, s));// && BotRole.CONTRIBUTOR.hasRequiredRole(user, channel.getGuild())
        else maker.appendRaw(s);
    }
}
