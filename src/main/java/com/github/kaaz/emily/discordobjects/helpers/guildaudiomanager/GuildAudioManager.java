package com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager;

import com.github.kaaz.emily.audio.configs.guild.PlaylistActiveConfig;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.guild.GuildActivePlaylistConfig;
import com.github.kaaz.emily.config.configs.guild.GuildLanguageConfig;
import com.github.kaaz.emily.discordobjects.exception.MissingPermException;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.VoiceChannel;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.ConfigValueChangeEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordVoiceLeave;
import com.github.kaaz.emily.exeption.BotException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.exeption.PermissionsException;
import com.github.kaaz.emily.launcher.Launcher;
import com.github.kaaz.emily.service.services.MusicDownloadService;
import com.github.kaaz.emily.service.services.ScheduleService;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 3/28/2017.
 */
public class GuildAudioManager {
    private static final Map<String, GuildAudioManager> MAP = new ConcurrentHashMap<>();
    static {
        AtomicInteger integer = new AtomicInteger();
        Launcher.registerStartup(() -> ConfigHandler.getNonDefaultSettings(PlayQueueConfig.class).forEach((channel, tracks) -> {
            ScheduleService.schedule(integer.getAndIncrement() + 5_000, () -> {
                boolean hasUser = false;
                for (User user : channel.getConnectedUsers()){
                    if (!user.isBot()) {
                        hasUser = true;
                        break;
                    }
                }
                if (hasUser) return;
                GuildAudioManager manager = getManager(channel);
                manager.queue.addAll(tracks);
            });
        }));
        AbstractConfig<List<Track>, VoiceChannel> config = ConfigHandler.getConfig(PlayQueueConfig.class);
        Launcher.registerShutdown(() -> MAP.forEach((s, guildAudioManager) -> config.setValue(guildAudioManager.channel, guildAudioManager.queue)));
    }
    public static void init(){}
    public static GuildAudioManager getManager(VoiceChannel channel, boolean make){
        if (make) return MAP.computeIfAbsent(channel.getGuild().getID(), s -> new GuildAudioManager(channel));
        else return MAP.get(channel.getGuild().getID());
    }
    public static GuildAudioManager getManager(VoiceChannel channel){
        return getManager(channel, true);
    }
    public static GuildAudioManager getManager(Guild guild){
        return MAP.get(guild.getID());
    }
    private VoiceChannel channel;
    private AudioPlayer player;
    private final List<File> speeches = new ArrayList<>(1);
    private final List<Track> queue = new ArrayList<>();
    private final AtomicReference<Track> currentTrack = new AtomicReference<>();
    private File paused, current;
    private long pausePosition;
    private boolean leaveAfterThis;
    private GuildAudioManager(VoiceChannel channel) {
        this.channel = channel;
        this.player = new AudioPlayer(this.channel.getGuild().guild());
        this.player.setVolume(ConfigHandler.getSetting(VolumeConfig.class, channel.getGuild()));
        try{if (this.player.getGuild().getConnectedVoiceChannel() == null) this.channel.join();
        } catch (MissingPermException e) {
            throw new PermissionsException("Insufficient permission to join you");
        }
    }
    public void leave(){
        MAP.remove(this.player.getGuild().getStringID());
        this.player.clean();
        this.channel.leave();
    }
    public void pause(boolean pause){
        this.player.setPaused(pause);
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
        MusicDownloadService.ensureDownload(this.queue.size() < 1 ? 0 : 1, track, t -> {
            if (this.current == null){
                this.start(track.file(), 0);
            }else{
                this.queue.add(track);
            }
        });
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
    public void setPlaylistOn(boolean on){
        ConfigHandler.setSetting(PlaylistActiveConfig.class, this.channel.getGuild(), on);
    }
    private void start(File file, int position){
        this.current = file;
        this.player.setPaused(true);
        this.player.clear();
        try{this.player.queue(file);
        } catch (IOException e) {
            throw new BotException("IOException", e);
        } catch (UnsupportedAudioFileException e) {
            throw new DevelopmentException("Unsupported file: " + file.getName(), e);
        }
        if (position != 0){
            this.player.skipTo(position);
        }
        this.player.setPaused(false);
    }
    public void skipTrack() {
        this.onFinish();
    }
    private void onFinish(){
        if (leaveAfterThis){
            this.leave();
            return;
        }
        if (this.speeches.size() > 0){
            this.start(this.speeches.get(0), 0);
        }else if (this.paused != null){
            this.start(this.paused, (int) this.pausePosition);
        }else if (this.queue.size() > 0){
            MusicDownloadService.ensureDownload(0, this.queue.get(0), track -> this.start(track.file(), 0));
            this.currentTrack.set(this.queue.remove(0));// duplicate download attempt safe
        }else if (ConfigHandler.getSetting(PlaylistActiveConfig.class, this.channel.getGuild())){
            this.queueTrack(ConfigHandler.getSetting(GuildActivePlaylistConfig.class, Guild.getGuild(this.player.getGuild())).getNext());
        }
    }
    public void leaveAfterThis(){
        this.leaveAfterThis = true;
    }
    public Track currentTrack() {
        return this.currentTrack.get();
    }
    public long currentTime(){
        return this.player.getCurrentTrack().getCurrentTrackTime();
    }
    public List<Track> getQueue() {
        return this.queue;
    }
    public VoiceChannel voiceChannel() {
        return this.channel;
    }
    public boolean isPaused() {
        return this.player.isPaused();
    }
    @EventListener
    public static void handle(TrackFinishEvent event){
        GuildAudioManager manager = MAP.get(event.getPlayer().getGuild().getStringID());
        if (manager != null) manager.onFinish();
    }
    @EventListener
    public static void handle(DiscordVoiceLeave event){
        GuildAudioManager manager = MAP.get(event.getGuild().getID());
        if (manager == null) return;
        for (User user : event.getChannel().getConnectedUsers()){
            if (!user.isBot()) return;
        }
        manager.leave();
    }
    @EventListener
    public static void handle(ConfigValueChangeEvent event){
        if (!event.getConfigType().equals(VolumeConfig.class)) return;
        GuildAudioManager manager = getManager(((Guild) event.getConfigurable()));
        if (manager != null) manager.player.setVolume(((Integer) event.getNewValue()) / 100f);
    }
}
