package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.audio.configs.track.PlayCountConfig;
import com.github.nija123098.evelyn.audio.configs.track.TrackTimeExpireConfig;
import com.github.nija123098.evelyn.audio.configs.track.TrackTypeConfig;
import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.CareLess;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.ThreadHelper;
import com.github.nija123098.evelyn.util.YTDLHelper;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An abstract Track type which describes a track that
 * could be downloaded for efficiency as to not expend
 * an excessive amount of total bandwidth usage.  These
 * tracks should only be used when above a request threshold.
 *
 * @author nija123098
 * @since 1.0.0
 */
public abstract class DownloadableTrack extends Track {
    private static final List<String> TYPES = Stream.of(ConfigProvider.AUDIO_SETTINGS.audioFileTypes().split(",")).map(String::trim).collect(Collectors.toList());
    private static final LocalAudioSourceManager LOCAL_SOURCE_MANAGER = new LocalAudioSourceManager();
    public static AudioTrack makeAudioTrack(File file) {
        if (file == null) return null;
        return (AudioTrack) LOCAL_SOURCE_MANAGER.loadItem(GuildAudioManager.PLAYER_MANAGER, new AudioReference(file.getAbsolutePath(), null));
    }
    public static List<Track> getDownloadedTracks() {
        File[] downloadedMusic = new File(ConfigProvider.FOLDER_SETTINGS.audioFolder()).listFiles();
        if (downloadedMusic == null) return Collections.emptyList();// happens when no music is downloaded
        return Stream.of(downloadedMusic).filter(File::isFile).filter(file -> TYPES.contains(file.getName().split(Pattern.quote("."))[1])).map(File::getName).map(s -> getTrack(s.substring(0, s.indexOf('.')))).filter(track -> MusicDownloadService.isDownloaded(((DownloadableTrack) track))).collect(Collectors.toList());
    }
    private transient Long length;
    DownloadableTrack(String id) {
        super(id);
    }
    DownloadableTrack() {}
    public boolean isDownloaded() {
        return MusicDownloadService.isDownloaded(this);
    }
    public AudioTrack getAudioTrack(GuildAudioManager manager) {
        if (this.isDownloaded()) return makeAudioTrack(file());
        int playTimes = ConfigHandler.getSetting(PlayCountConfig.class, this);
        if (playTimes >= ConfigProvider.AUDIO_SETTINGS.requiredPlaysToDownload() && ConfigProvider.AUDIO_SETTINGS.requiredPlaysToDownload() != -1) MusicDownloadService.queueDownload(playTimes == 0 ? 0 : (int) Math.log(playTimes), this, manager == null ? null : downloadableTrack -> manager.swap(this));
        Object lock = new Object();
        AtomicReference<AudioTrack> reference = new AtomicReference<>();
        AtomicReference<FriendlyException> exceptionReference = new AtomicReference<>();
        ThreadHelper.enableInterruptLogging(Thread.currentThread());
        GuildAudioManager.PLAYER_MANAGER.loadItem(this.getSource(), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                reference.set(track);
                CareLess.lessSleep(20);// allow locking time
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
            @Override public void playlistLoaded(AudioPlaylist playlist) {}
            @Override public void noMatches() {}
            @Override public void loadFailed(FriendlyException exception) {
                exceptionReference.set(exception);
                CareLess.lessSleep(20);// allow locking time
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        });
        try{
            synchronized (lock) {
                lock.wait();
            }
            if (exceptionReference.get() != null) throw new DevelopmentException("Exception loading track", exceptionReference.get());
            if (this.length == null) this.length = reference.get().getDuration();
            ThreadHelper.disableInterruptLogging(Thread.currentThread());
            return reference.get();
        } catch (InterruptedException e) {
            Log.log("Exception loading AudioTrack for " + this.getID(), exceptionReference.get() == null ? e : exceptionReference.get());
        }
        return null;
    }
    @Override
    public void manage() {
        super.manage();
        if (MusicDownloadService.isDownloaded(this) && ConfigHandler.getSetting(TrackTimeExpireConfig.class, this) + ConfigProvider.AUDIO_SETTINGS.trackExpirationTime() < System.currentTimeMillis()) {
            this.file().delete();
            ConfigHandler.setSetting(TrackTypeConfig.class, this, null);
        }
    }
    public boolean download() {
        return this.isDownloaded() || this.actualDownload();
    }
    private static final File AUDIO_CONTAINER = new File(ConfigProvider.FOLDER_SETTINGS.audioFolder());
    boolean actualDownload() {
        if (YTDLHelper.download(this.getSource(), this.getID(), this.getPreferredType())) {
            ConfigHandler.setSetting(TrackTypeConfig.class, this, Stream.of(AUDIO_CONTAINER.listFiles()).filter(Objects::nonNull).filter(File::isFile).filter(file -> file.getName().startsWith(this.getID())).filter(file -> TYPES.contains(file.getName().split(Pattern.quote("."))[1])).findAny().get().getName().split(Pattern.quote("."))[1]);
            return true;
        }
        return false;
    }
    public File file() {// used when playing, and deleting, but at deletion it does not matter
        ConfigHandler.setSetting(TrackTimeExpireConfig.class, this, System.currentTimeMillis());
        return new File(ConfigProvider.FOLDER_SETTINGS.audioFolder() + this.getID() + "." + ConfigHandler.getSetting(TrackTypeConfig.class, this));
    }
    public String getPreferredType() {
        return ConfigProvider.AUDIO_SETTINGS.audioFormat();
    }
    @Override
    public Long getLength() {
        return this.length == null ? this.getAudioTrack(null).getDuration() : this.length;
    }
}
