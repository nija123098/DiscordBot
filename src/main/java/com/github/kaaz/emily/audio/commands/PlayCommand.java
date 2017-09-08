package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.audio.configs.track.TrackAssociationConfig;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.YTUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 6/8/2017.
 */
public class PlayCommand extends AbstractCommand {
    private static final Map<User, Track> PREVIOUS_MAP = new ConcurrentHashMap<>();
    public PlayCommand() {
        super("play", ModuleLevel.MUSIC, "p", null, "Plays music from youtube, soundcloud or a stream from Twitch");
    }
    @Command
    public void command(GuildAudioManager manager, @Argument(info = "a song name or url") String s, User user, MessageMaker maker) {
        List<Track> trackList = Track.getTracks(s);
        try {
            if (trackList.isEmpty() && !(s == null || s.isEmpty())) {
                Track t = YTUtil.getTrack(s);
                if (t == null) throw new ArgumentException("I couldn't find a track for that");
                trackList.add(t);
                Track track = PREVIOUS_MAP.get(user);
                if (trackList.size() > 1){
                    for (int i = 0; i < trackList.size(); i++) {
                        for (int j = 0; j < i; j++) {
                            TrackAssociationConfig.associate(trackList.get(i), trackList.get(j));
                        }
                    }
                }
                else if (track != null) TrackAssociationConfig.associate(track, trackList.get(0));
                PREVIOUS_MAP.put(user, trackList.get(trackList.size() - 1));
            }
        } catch (YTUtil.YoutubeSearchException e){
            Log.log("Exception doing YouTube search", e);
            maker.append("Something went wrong with YouTube searching, I'm going to start playing music like nothing happened");
        }
        trackList.forEach(manager::queueTrack);
    }
}
