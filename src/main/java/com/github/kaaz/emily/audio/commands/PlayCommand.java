package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.audio.TwitchTrack;
import com.github.kaaz.emily.audio.YoutubeTrack;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.util.SCUtil;
import com.github.kaaz.emily.util.TwitchUtil;
import com.github.kaaz.emily.util.YTSearch;
import com.github.kaaz.emily.util.YTUtil;

import java.util.Collections;
import java.util.List;

/**
 * Made by nija123098 on 6/8/2017.
 */
public class PlayCommand extends AbstractCommand {
    public PlayCommand() {
        super("play", ModuleLevel.MUSIC, null, null, "PLays music from youtube or soundcloud");
    }
    @Command
    public void command(GuildAudioManager manager, @Argument(info = "a song name or url") String s, MessageMaker maker) {
        List<Track> tracks = getTracksToPlay(s);
        if (tracks != null) tracks.forEach(manager::queueTrack);
        else maker.append("I don't even know what you could have even entered to get this result");
    }
    public static List<Track> getTracksToPlay(String s){// may want to move
        String code = YTUtil.extractVideoCode(s);
        if (code == null) {
            YTSearch.SimpleResult result = YTSearch.getResults(s);
            if (result != null) code = result.getCode();
        }
        if (code != null) {
            return Collections.singletonList(Track.getTrack(YoutubeTrack.class, code));
        }
        code = YTUtil.extractPlaylistCode(s);
        if (code != null){
            return YTUtil.getTracksFromPlaylist(s);
        }
        List<Track> tracks = SCUtil.extractTracks(s);
        if (tracks != null){
            return tracks;
        }
        code = TwitchUtil.extractCode(s);
        if (code != null){
            Collections.singletonList(Track.getTrack(TwitchTrack.class, code));
        }
        return Collections.emptyList();
    }
}
