package com.github.kaaz.emily.discordobjects.helpers;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.guild.GuildLanguageConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.service.services.MusicDownloadService;
import com.github.kaaz.emily.util.LangString;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.SpeechHelper;
import sx.blah.discord.util.audio.AudioPlayer;
import sx.blah.discord.util.audio.events.TrackFinishEvent;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 3/28/2017.
 */
public class GuildAudioManager {
    private static final Map<String, GuildAudioManager> MAP = new ConcurrentHashMap<>();
    public static GuildAudioManager getManager(Guild guild, boolean make){
        if (make){
            return MAP.computeIfAbsent(guild.getID(), s -> new GuildAudioManager(guild));
        }else{
            if (MAP.containsKey(guild.getID())){
                return MAP.get(guild.getID());
            }
            return null;
        }
    }
    public static GuildAudioManager getManager(Guild guild){
        return getManager(guild, true);
    }
    private AudioPlayer player;
    private List<File> speeches = new ArrayList<>(1);
    private List<Track> queue = new ArrayList<>();
    private File paused, current;
    private long pausePosition;
    private boolean downloading;
    private GuildAudioManager(Guild guild) {
        this.player = new AudioPlayer(guild.guild());
    }
    public void clearQueue(){
        this.queue.clear();
    }
    public void queueSpeach(LangString string){
        File file = SpeechHelper.getFile(string, ConfigHandler.getSetting(GuildLanguageConfig.class, Guild.getGuild(this.player.getGuild())));
        if (this.current == null){
            this.start(file, 0);
        }else{
            this.speeches.add(file);
        }
    }
    public void queueTrack(Track track){
        if (!track.isDownloaded()){
            MusicDownloadService.queueDownload(track, this.queue.size() > 0 ? t -> {
                this.downloading = true;
                this.queueTrack(track);
            } : t -> {});
            return;
        }
        if (this.current == null && !this.downloading){
            this.start(track.file(), 0);
        }else{
            this.queue.add(track);
        }
    }
    public void interupt(LangString langString){// must fully exist
        File file = SpeechHelper.getFile(langString, ConfigHandler.getSetting(GuildLanguageConfig.class, Guild.getGuild(this.player.getGuild())));
        if (this.paused != null){
            Log.log("Attempted to interrupt an interrupt, ignoring: " + file.getPath());
            return;// might not want to ignore an interrupt during an interrupt
        }
        this.player.setPaused(true);
        this.pausePosition = this.player.getCurrentTrack().getCurrentTrackTime();
        this.paused = this.current;
        this.pausePosition = this.player.getCurrentTrack().getCurrentTrackTime();
        this.player.skip();
        start(file, 0);
    }
    private void start(File file, int position){
        this.current = file;
        this.player.setPaused(true);
        try{this.player.queue(file);
        } catch (IOException e) {
            Log.log("IOE", e);
        } catch (UnsupportedAudioFileException e) {
            Log.log("Unsupported file: " + file.getName(), e);
        }
        if (position != 0){
            this.player.skipTo(position);
        }
        this.player.setPaused(false);
    }
    private void onFinish(){
        if (this.speeches.size() > 0){
            this.start(this.speeches.get(0), 0);
        } else if (this.paused != null){
            this.start(this.paused, (int) this.pausePosition);
        }else if (this.queue.size() > 0){
            MusicDownloadService.queueDownload(this.queue.get(0), track -> this.start(this.queue.get(0).file(), 0));
        }// duplicate download attempt safe
    }
    @EventListener
    public static void handle(TrackFinishEvent event){
        GuildAudioManager manager = getManager(Guild.getGuild(event.getPlayer().getGuild()), false);
        if (manager != null){
            manager.onFinish();
        }
    }
}
