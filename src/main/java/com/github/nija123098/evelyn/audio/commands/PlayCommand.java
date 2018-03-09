package com.github.nija123098.evelyn.audio.commands;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.audio.configs.track.TrackAssociationConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.YTUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlayCommand extends AbstractCommand {
    private static final Map<User, Track> PREVIOUS_MAP = new ConcurrentHashMap<>();

    public PlayCommand() {
        super("play", ModuleLevel.MUSIC, "p", null, "Plays music from youtube, soundcloud, or a stream from Twitch");
    }

    @Command
    public void command(GuildAudioManager manager, @Argument(info = "a song name or url") String s, User user, MessageMaker maker) {
        List<Track> trackList = Track.getTracks(s);
        try {
            if (trackList.isEmpty() && !(s == null || s.isEmpty())) {
                Track t = Track.getTrack(s);
                if (t == null) throw new ArgumentException("I couldn't find a track for that");
                if (!t.isAvailable()) throw new ArgumentException("That track is no longer available");
                maker.withDeleteDelay(10_000L).appendRaw(t.getName());
                trackList.add(t);
                Track track = PREVIOUS_MAP.get(user);
                if (trackList.size() > 1) {
                    for (int i = 0; i < trackList.size(); i++) {
                        for (int j = 0; j < i; j++) {
                            TrackAssociationConfig.associate(trackList.get(i), trackList.get(j));
                        }
                    }
                } else if (track != null) TrackAssociationConfig.associate(track, trackList.get(0));
                PREVIOUS_MAP.put(user, trackList.get(trackList.size() - 1));
            }
        } catch (YTUtil.YoutubeSearchException e) {
            Log.log("Exception doing YouTube search", e);
            maker.append("Something went wrong with YouTube searching, I'm going to start playing music like nothing happened").mustEmbed();
        }
        trackList.forEach(manager::queueTrack);
    }

    @Override
    public boolean useReactions() {
        return true;
    }
}
