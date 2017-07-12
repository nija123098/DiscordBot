package com.github.kaaz.emily.audio;

import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

/**
 * Made by nija123098 on 6/11/2017.
 */
public class TwitchTrack extends Track {
    private static final TwitchStreamAudioSourceManager TWITCH_AUDIO_SOURCE_MANAGER = new TwitchStreamAudioSourceManager();
    static {
        Track.registerTrackType(TwitchTrack.class, TwitchTrack::new, TwitchTrack::new);
    }
    TwitchTrack() {}
    TwitchTrack(String id) {
        super(id);
    }
    @Override
    public String getSource() {
        return "https://www.twitch.tv/" + this.getCode();
    }
    @Override
    public String previewURL() {
        return null;
    }
    @Override
    public AudioTrack getTrack() {
        AudioItem audioItem = TWITCH_AUDIO_SOURCE_MANAGER.loadItem(GuildAudioManager.PLAYER_MANAGER, new AudioReference(this.getSource(), null));
        return (AudioTrack) audioItem;
    }
    @Override
    public Long getLength() {
        return null;
    }
}
