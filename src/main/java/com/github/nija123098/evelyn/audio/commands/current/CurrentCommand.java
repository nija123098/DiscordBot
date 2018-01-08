package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.audio.SpeechTrack;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildActivePlaylistConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exeption.ContextException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.Time;

/**
 * Made by nija123098 on 5/24/2017.
 */
public class CurrentCommand extends AbstractCommand {
    private static final String NOTES = EmoticonHelper.getChars("notes", true);
    public CurrentCommand() {
        super("current", ModuleLevel.MUSIC, "playing, nowplaying, np", null, "Retrieves information about the song currently playing");
    }
    @Command
    public static void command(Guild guild, MessageMaker maker, @Context(softFail = true) Track track){
        GuildAudioManager manager = GuildAudioManager.getManager(guild);
        if (manager == null) throw new ContextException("I am not playing any music here");
        if (track == null) {
            maker.append("Nothing is currently playing");
            return;
        }
        if (track instanceof SpeechTrack){
            maker.append("Don't you recognize my voice?");
            return;
        }
        long time = manager.currentTime();
        maker.getTitle().appendRaw(NOTES + " " + track.getName());
        maker.appendRaw(FormatHelper.embedLink("source", track.getSource()) + " | pl " + ConfigHandler.getSetting(GuildActivePlaylistConfig.class, manager.getGuild()).getName());
        maker.withUrl(track.getSource());
        maker.withThumb(track.getPreviewURL());
        maker.withReaction("track_next");
        maker.withReaction("pause_button");
        if (manager.voiceChannel().getConnectedUsers().stream().filter(user -> BotRole.BOT_ADMIN.hasRequiredRole(user, null)).count() != 0) maker.withReaction("no_entry_sign");
        Long length = track.getLength();
        if (length != null) maker.getNewFieldPart().withBoth("Duration", Time.getAbbreviatedMusic(length, time));
        if (manager.getNext(false) != null) maker.getNewFieldPart().withBoth("Next", manager.getNext(false).getName());
    }
    static String getPlayBar(boolean paused, long current, long total){
        int first = (int) ((float) current/total * 10);
        return EmoticonHelper.getChars(paused ? "pause_button" : "play_button", true)
                + FormatHelper.repeat('▬', first)
                + EmoticonHelper.getChars("radio_button", true)
                + FormatHelper.repeat('▬', 10 - first)
                + "[" + Time.getAbbreviatedMusic(total, current) + "]";
    }
}
