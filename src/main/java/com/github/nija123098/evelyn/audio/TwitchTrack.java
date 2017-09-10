package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.launcher.BotConfig;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.handlers.UserResponseHandler;
import com.mb3364.twitch.api.models.User;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Made by nija123098 on 6/11/2017.
 */
public class TwitchTrack extends Track {
    private static final TwitchStreamAudioSourceManager TWITCH_AUDIO_SOURCE_MANAGER = new TwitchStreamAudioSourceManager();
    private static final Twitch TWITCH = new Twitch();
    static {
        TWITCH.setClientId(BotConfig.TWITCH_ID);
        Track.registerTrackType(TwitchTrack.class, TwitchTrack::new, TwitchTrack::new);
    }
    TwitchTrack(String id) {
        super(id);
    }
    @Override
    public String getSource() {
        return "https://www.twitch.tv/" + this.getCode();
    }
    TwitchTrack() {}
    private transient String previewURL;
    @Override
    public String getPreviewURL() {
        if (this.previewURL != null) return this.previewURL;
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        TWITCH.users().get(this.getCode(), new UserResponseHandler() {
            @Override
            public void onSuccess(User user) {
                queue.add(user.getLogo());
            }
            @Override
            public void onFailure(int i, String s, String s1){queue.add(null);}
            @Override
            public void onFailure(Throwable throwable) {queue.add(null);}
        });
        try{return this.previewURL = queue.take();
        }catch(InterruptedException ignored){}
        return null;
    }
    private transient String info;
    @Override
    public String getInfo() {
        if (this.info != null) return this.info;
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        TWITCH.users().get(this.getCode(), new UserResponseHandler() {
            @Override
            public void onSuccess(User user) {
                queue.add(user.getBio());
            }
            @Override
            public void onFailure(int i, String s, String s1){queue.add(null);}
            @Override
            public void onFailure(Throwable throwable) {queue.add(null);}
        });
        try{return this.info = queue.take();
        }catch(InterruptedException ignored){}
        return null;
    }
    @Override
    public AudioTrack getAudioTrack(GuildAudioManager manager) {
        return (AudioTrack) TWITCH_AUDIO_SOURCE_MANAGER.loadItem(GuildAudioManager.PLAYER_MANAGER, new AudioReference(this.getSource(), null));
    }
    @Override
    public Long getLength() {
        return null;
    }
}
