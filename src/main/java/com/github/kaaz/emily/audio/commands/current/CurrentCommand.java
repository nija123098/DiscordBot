package com.github.kaaz.emily.audio.commands.current;

import com.github.kaaz.emily.audio.SpeechTrack;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.guild.GuildActivePlaylistConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Time;

/**
 * Made by nija123098 on 5/24/2017.
 */
public class CurrentCommand extends AbstractCommand {
    private static final String NOTES = EmoticonHelper.getChars("notes", true);
    public CurrentCommand() {
        super("current", ModuleLevel.MUSIC, "playing, nowplaying, np", null, "Retrieves information about the song currently playing");
    }
    @Command
    public static void command(GuildAudioManager manager, Guild guild, MessageMaker maker){
        Track track = manager.currentTrack();
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
        maker.appendRaw("[source](" + track.getSource() + ") - " + ConfigHandler.getSetting(GuildActivePlaylistConfig.class, guild).getName());
        maker.withUrl(track.getSource());
        maker.withThumb(track.getPreviewURL());
        maker.withReaction("track_next");
        if (manager.voiceChannel().getConnectedUsers().stream().filter(user -> BotRole.BOT_ADMIN.hasRequiredRole(user, null)).count() != 0) maker.withReaction("no_entry_sign");
        Long length = track.getLength();
        if (length != null) maker.getNewFieldPart().withBoth("Duration", Time.getAbbreviatedMusic(length, time));
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
