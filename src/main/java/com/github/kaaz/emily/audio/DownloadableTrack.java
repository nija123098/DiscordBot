package com.github.kaaz.emily.audio;

import com.github.kaaz.emily.audio.configs.track.*;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.service.services.MusicDownloadService;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.YTDLHelper;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 6/10/2017.
 */
public abstract class DownloadableTrack extends Track {
    private static final List<String> TYPES = Stream.of(BotConfig.AUDIO_FILE_TYPES.split(",")).map(String::trim).collect(Collectors.toList());
    private static final LocalAudioSourceManager LOCAL_SOURCE_MANAGER = new LocalAudioSourceManager();
    public static AudioTrack makeAudioTrack(File file){
        if (file == null) return null;
        return (AudioTrack) LOCAL_SOURCE_MANAGER.loadItem(GuildAudioManager.PLAYER_MANAGER, new AudioReference(file.getAbsolutePath(), null));
    }
    public static List<Track> getDownloadedTracks(){
        File[] downloadedMusic = new File(BotConfig.AUDIO_PATH).listFiles();
        if (downloadedMusic == null) return Collections.emptyList();// happens when no music is downloaded
        return Stream.of(downloadedMusic).filter(File::isFile).filter(file -> TYPES.contains(file.getName().split(Pattern.quote("."))[1])).map(File::getName).map(s -> Track.getTrack(s.substring(0, s.indexOf('.')))).filter(track -> MusicDownloadService.isDownloaded(((DownloadableTrack) track))).collect(Collectors.toList());
    }
    DownloadableTrack(String id) {
        super(id);
    }
    DownloadableTrack() {}
    public boolean isDownloaded(){
        return MusicDownloadService.isDownloaded(this);
    }
    public AudioTrack getAudioTrack(GuildAudioManager manager){
        if (this.isDownloaded()) return makeAudioTrack(file());
        int playTimes = ConfigHandler.getSetting(PlayCountConfig.class, this);
        if (playTimes >= BotConfig.REQUIRED_PLAYS_TO_DOWNLOAD) MusicDownloadService.queueDownload(playTimes == 0 ? 0 : (int) Math.log(playTimes), this, manager == null ? null : downloadableTrack -> {
            if (manager.currentTrack().equals(this)) manager.swap(this.getAudioTrack(null));
        });
        BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>(1);
        GuildAudioManager.PLAYER_MANAGER.loadItem(this.getSource(), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                queue.add(track);
            }
            @Override public void playlistLoaded(AudioPlaylist playlist) {}
            @Override public void noMatches() {}
            @Override public void loadFailed(FriendlyException exception) {}
        });
        try{return queue.take();
        } catch (InterruptedException e) {
            Log.log("Exception loading AuidoTrack for " + this.getID(), e);
        }
        return null;
    }
    @Override
    public void manage(){
        super.manage();
        if (MusicDownloadService.isDownloaded(this) && ConfigHandler.getSetting(TrackTimeExpireConfig.class, this) > ConfigHandler.getSetting(TrackDeleteTimeConfig.class, GlobalConfigurable.GLOBAL) + System.currentTimeMillis()){
            this.file().delete();
            ConfigHandler.setSetting(DurationTimeConfig.class, this, null);
            ConfigHandler.setSetting(TrackTypeConfig.class, this, null);
        }
    }
    public boolean download() {
        return this.isDownloaded() || this.actualDownload();
    }
    private static final File AUDIO_CONTAINER = new File(BotConfig.AUDIO_PATH);
    private boolean actualDownload(){
        if (YTDLHelper.download(this.getSource(), this.getID(), this.getPreferredType())){
            ConfigHandler.setSetting(TrackTypeConfig.class, this, Stream.of(AUDIO_CONTAINER.listFiles()).filter(Objects::nonNull).filter(File::isFile).filter(file -> file.getName().startsWith(this.getID())).filter(file -> TYPES.contains(file.getName().split(Pattern.quote("."))[1])).findAny().get().getName().split(Pattern.quote("."))[1]);
            ConfigHandler.setSetting(DurationTimeConfig.class, this, getLength());
            return true;
        }
        return false;
    }
    public File file(){// used when playing, and deleting, but at deletion it does not matter
        return new File(BotConfig.AUDIO_PATH + this.getID() + "." + ConfigHandler.getSetting(TrackTypeConfig.class, this));
    }
    public String getPreferredType(){
        return BotConfig.AUDIO_FORMAT;
    }
    @Override
    public Long getLength() {
        return this.isDownloaded() ? ConfigHandler.getSetting(DurationTimeConfig.class, this) : this.getAudioTrack(null).getDuration();
    }
}
