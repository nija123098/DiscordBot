package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.YTUtil;

import java.util.List;

/**
 * Made by nija123098 on 6/8/2017.
 */
public class PlayCommand extends AbstractCommand {
    public PlayCommand() {
        super("play", ModuleLevel.MUSIC, null, null, "Pays music from youtube or soundcloud or a stream from Twitch");
    }
    @Command
    public void command(GuildAudioManager manager, @Argument(info = "a song name or url") String s, MessageMaker maker) {
        List<Track> trackList = Track.getTracks(s);
        try{if (trackList.isEmpty() && !(s == null || s.isEmpty())) {
            Track t = YTUtil.getTrack(s);
            if (t != null) trackList.add(t);
        }
        } catch (YTUtil.YoutubeSearchException e){
            Log.log("Exception doing YouTube search", e);
            maker.append("Something went wrong with YouTube searching, I'm going to start playing music like nothing happened");
        }
        trackList.forEach(manager::queueTrack);
    }
}
