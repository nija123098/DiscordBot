package com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager;

import com.github.kaaz.emily.audio.SpeechTrack;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.audio.configs.guild.QueueTrackOnlyConfig;
import com.github.kaaz.emily.audio.configs.guild.SkipPercentConfig;
import com.github.kaaz.emily.audio.configs.track.PlayCountConfig;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.guild.GuildActivePlaylistConfig;
import com.github.kaaz.emily.config.configs.guild.GuildLanguageConfig;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.VoiceChannel;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.ConfigValueChangeEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordVoiceLeave;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.launcher.Launcher;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.LangString;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.SpeechHelper;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import org.eclipse.jetty.util.ConcurrentHashSet;
import sx.blah.discord.handle.audio.AudioEncodingType;
import sx.blah.discord.handle.audio.IAudioProvider;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 3/28/2017.
 */
public class GuildAudioManager extends AudioEventAdapter{
    private static final Map<String, GuildAudioManager> MAP = new ConcurrentHashMap<>();
    public static final DefaultAudioPlayerManager PLAYER_MANAGER = new DefaultAudioPlayerManager();
    static {
        PLAYER_MANAGER.registerSourceManager(new YoutubeAudioSourceManager(false));
        PLAYER_MANAGER.registerSourceManager(new SoundCloudAudioSourceManager(false));
        PLAYER_MANAGER.registerSourceManager(new LocalAudioSourceManager());
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
        PLAYER_MANAGER.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
        PLAYER_MANAGER.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        AbstractConfig<List<Track>, VoiceChannel> config = ConfigHandler.getConfig(PlayQueueConfig.class);
        LangString bye = new LangString(false, "I have to go restart, I will be back soon.");
        Launcher.registerShutdown(() -> MAP.forEach((s, guildAudioManager) -> {
            config.setValue(guildAudioManager.channel, guildAudioManager.queue);
            guildAudioManager.interrupt(bye);
        }));
        EventDistributor.register(GuildAudioManager.class);
    }
    public static void init(){}
    public static GuildAudioManager getManager(VoiceChannel channel, boolean make){
        if (channel == null) return null;
        if (make) {
            GuildAudioManager current = getManager(channel.getGuild());
            if (current != null && !current.channel.equals(channel)) throw new ArgumentException("You must be in the voice channel with " + DiscordClient.getOurUser().getDisplayName(channel.getGuild()) + " to use that command");
            AtomicReference<VoiceChannel> reference = new AtomicReference<>(channel);
            return MAP.computeIfAbsent(channel.getGuild().getID(), s -> new GuildAudioManager(reference.get()));
        } else return MAP.get(channel.getGuild().getID());
    }
    public static GuildAudioManager getManager(VoiceChannel channel){
        return getManager(channel, true);
    }
    public static GuildAudioManager getManager(Guild guild){
        return MAP.get(guild.getID());
    }
    private VoiceChannel channel;
    private AudioPlayer lavaPlayer;
    private final Set<User> skipSet = new ConcurrentHashSet<>();
    private final List<File> speeches = new CopyOnWriteArrayList<>();
    private final List<Track> queue = new CopyOnWriteArrayList<>();
    private Track paused, current;
    private long pausePosition;
    private boolean leaveAfterThis, loop;
    private GuildAudioManager(VoiceChannel channel) {
        this.channel = channel;
        this.lavaPlayer = PLAYER_MANAGER.createPlayer();
        this.lavaPlayer.addListener(this);
        this.channel.channel().getGuild().getAudioManager().setAudioProvider(new AudioProvider(this.lavaPlayer));
        this.lavaPlayer.setVolume(ConfigHandler.getSetting(VolumeConfig.class, channel.getGuild()));
        if (this.channel.getGuild().getConnectedVoiceChannel() == null) this.channel.join();
    }
    public void leave(){
        MAP.remove(this.channel.getGuild().getID());
        this.lavaPlayer.destroy();
        this.channel.leave();
    }
    public void pause(boolean pause){
        this.lavaPlayer.setPaused(pause);
    }
    public void clearQueue(){
        this.queue.clear();
    }
    public void queueSpeach(LangString string){
        File file = SpeechHelper.getFile(string, ConfigHandler.getSetting(GuildLanguageConfig.class, this.channel.getGuild()));
        if (this.current == null){
            this.start(new SpeechTrack(file), 0);
        }else{
            this.speeches.add(file);
        }
    }
    public void queueTrack(Track track){// todo ensure correct order
        if (this.current == null){
            this.start(track, 0);
        }else if (track != null){
            this.queue.add(track);
        }else this.current = null;
    }
    public void interrupt(LangString langString){// must fully exist
        File file = SpeechHelper.getFile(langString, ConfigHandler.getSetting(GuildLanguageConfig.class, this.channel.getGuild()));
        if (this.paused != null){
            Log.log("Attempted to interrupt an interrupt, ignoring: " + file.getPath());
            return;// might not want to ignore an interrupt during an interrupt
        }
        this.lavaPlayer.setPaused(true);
        this.pausePosition = this.lavaPlayer.getPlayingTrack().getPosition();
        this.paused = this.current;
        this.lavaPlayer.stopTrack();
        start(new SpeechTrack(file), 0);
    }
    public void setPlaylistOn(boolean on){
        ConfigHandler.setSetting(QueueTrackOnlyConfig.class, this.channel.getGuild(), on);
    }
    private void start(Track track, int position){
        this.skipSet.clear();
        this.current = track;
        this.lavaPlayer.setPaused(true);
        this.lavaPlayer.startTrack(this.current.getTrack(), true);
        if (position != 0) this.lavaPlayer.getPlayingTrack().setPosition(position);
        this.lavaPlayer.setPaused(false);
        if (!(track instanceof SpeechTrack)) ConfigHandler.changeSetting(PlayCountConfig.class, track, integer -> integer + 1);
    }
    public void seek(long time) {
        this.lavaPlayer.getPlayingTrack().setPosition(time);
    }
    public void skipTrack() {
        this.lavaPlayer.stopTrack();
    }
    private void onFinish(){
        if (this.leaveAfterThis){
            this.leave();
            return;
        }
        if (this.loop) this.lavaPlayer.playTrack(this.current.getTrack());
        else if (this.speeches.size() > 0){
            this.start(new SpeechTrack(this.speeches.remove(0)), 0);
        }else if (this.paused != null){
            this.start(this.paused, (int) this.pausePosition);
            this.paused = null;
        }else if (this.queue.size() > 0){
            this.start(this.queue.remove(0), 0);
        }else if (ConfigHandler.getSetting(QueueTrackOnlyConfig.class, this.channel.getGuild())){
            this.queueTrack(ConfigHandler.getSetting(GuildActivePlaylistConfig.class, this.channel.getGuild()).getNext());
        }else this.current = null;
    }
    public void leaveAfterThis(){
        this.leaveAfterThis = true;
    }
    public void loop(boolean loop){
        this.loop = loop;
    }
    public boolean isLooping() {
        return this.loop;
    }
    public Track currentTrack() {
        return this.current;
    }
    public long currentTime(){
        return this.lavaPlayer.getPlayingTrack().getPosition();
    }
    public List<Track> getQueue() {
        return this.queue;
    }
    public VoiceChannel voiceChannel() {
        return this.channel;
    }
    public boolean isPaused() {
        return this.lavaPlayer.isPaused();
    }
    public void checkSkip(){
        if (this.skipSet.size() / (float) this.channel.getConnectedUsers().size() >= ConfigHandler.getSetting(SkipPercentConfig.class, this.channel.getGuild()) / 100F){
            this.skipTrack();
        }
    }
    @EventListener
    public static void handle(DiscordVoiceLeave event){
        GuildAudioManager manager = MAP.get(event.getGuild().getID());
        if (manager == null) return;
        for (User user : event.getChannel().getConnectedUsers()){
            if (!user.isBot()) {
                manager.skipSet.remove(user);
                manager.checkSkip();
                return;
            }
        }
        manager.leave();
    }
    @EventListener
    public static void handle(ConfigValueChangeEvent event){
        if (!event.getConfigType().equals(VolumeConfig.class)) return;
        GuildAudioManager manager = getManager(((Guild) event.getConfigurable()));
        if (manager != null) manager.lavaPlayer.setVolume((int) event.getNewValue());
    }
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        this.onFinish();
    }
    public class AudioProvider implements IAudioProvider {
        private final AudioPlayer audioPlayer;
        private AudioFrame lastFrame;
        private AudioProvider(AudioPlayer audioPlayer) {
            this.audioPlayer = audioPlayer;
        }
        @Override
        public boolean isReady() {
            if (lastFrame == null) {
                lastFrame = audioPlayer.provide();
            }
            return lastFrame != null;
        }
        @Override
        public byte[] provide() {
            if (lastFrame == null) {
                lastFrame = audioPlayer.provide();
            }

            byte[] data = lastFrame != null ? lastFrame.data : null;
            lastFrame = null;

            return data;
        }
        @Override
        public int getChannels() {
            return 2;
        }
        @Override
        public AudioEncodingType getAudioEncodingType() {
            return AudioEncodingType.OPUS;
        }
    }
}
