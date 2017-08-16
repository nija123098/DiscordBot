package com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager;

import com.github.kaaz.emily.audio.SpeechTrack;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.audio.configs.guild.QueueTrackOnlyConfig;
import com.github.kaaz.emily.audio.configs.guild.SkipPercentConfig;
import com.github.kaaz.emily.audio.configs.track.PlayCountConfig;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.guild.GuildActivePlaylistConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.VoiceChannel;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordVoiceLeave;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.exeption.GhostException;
import com.github.kaaz.emily.favor.configs.derivation.ListenedCountConfig;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.launcher.Launcher;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.Care;
import com.github.kaaz.emily.util.LangString;
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
import sx.blah.discord.handle.obj.IVoiceState;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
        Launcher.registerStartup(() -> ConfigHandler.getNonDefaultSettings(PlayQueueConfig.class).forEach((channel, tracks) -> ScheduleService.schedule(integer.getAndIncrement() * 1000 + 5_000, () -> {
            if (!hasValidListeners(channel)) return;
            GuildAudioManager manager = getManager(channel);
            manager.queueTrack(tracks.remove(0));
            manager.queue.addAll(tracks);
        })));
        PLAYER_MANAGER.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
        PLAYER_MANAGER.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        AbstractConfig<List<Track>, VoiceChannel> config = ConfigHandler.getConfig(PlayQueueConfig.class);
        LangString bye = new LangString(true, "I have to go restart, I will be back soon.");
        Launcher.registerShutdown(() -> {
            MAP.forEach((s, guildAudioManager) -> {
                config.setValue(guildAudioManager.channel, guildAudioManager.queue);
                guildAudioManager.interrupt(bye);
            });
            Care.lessSleep(5_000);
        });
        EventDistributor.register(GuildAudioManager.class);
    }
    public static void init(){}
    public static GuildAudioManager getManager(VoiceChannel channel, boolean make){
        if (channel == null) return null;// this might not happen anymore
        if (BotConfig.GHOST_MODE) throw new GhostException();
        if (make) {
            GuildAudioManager current = getManager(channel.getGuild());
            if (current != null && !current.channel.equals(channel)) throw new ArgumentException("You must be in the voice channel with me to use that command");
            if (!hasValidListeners(channel)) throw new ArgumentException("Someone has to be able to hear me in that voice channel");
            AtomicReference<VoiceChannel> reference = new AtomicReference<>(channel);
            return MAP.computeIfAbsent(channel.getGuild().getID(), s -> new GuildAudioManager(reference.get()));
        } else {
            GuildAudioManager manager = MAP.get(channel.getGuild().getID());
            return manager != null && manager.channel.equals(channel) ? manager : null;
        }
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
    private final List<LangString> speeches = new CopyOnWriteArrayList<>();
    private final List<Track> queue = new CopyOnWriteArrayList<>();
    private final List<LangString> interups = new CopyOnWriteArrayList<>();
    private Track current, paused, next;
    private long pausePosition, lastSkip;
    private boolean leaveAfterThis, loop, leaving;
    private GuildAudioManager(VoiceChannel channel) {
        this.channel = channel;
        this.lavaPlayer = PLAYER_MANAGER.createPlayer();
        this.lavaPlayer.addListener(this);
        this.channel.channel().getGuild().getAudioManager().setAudioProvider(new AudioProvider(this.lavaPlayer));
        this.lavaPlayer.setVolume(ConfigHandler.getSetting(VolumeConfig.class, channel.getGuild()));
        this.channel.join();
        this.start(new SpeechTrack(new LangString(true, "Hello"), MessageMaker.getLang(null, this.channel)), 0);
    }
    public void leave(){
        if (this.leaving || !hasValidListeners(this.channel)){
            MAP.remove(this.channel.getGuild().getID());
            this.lavaPlayer.destroy();
            this.channel.leave();
        }else{
            this.clearQueue();
            this.speeches.clear();
            this.queueSpeech(new LangString(true, "Goodbye"));
            if (this.current != null) this.skipTrack();
            this.loop = false;
            this.leaveAfterThis = true;
            this.leaving = true;
        }
    }
    public void pause(boolean pause){
        this.lavaPlayer.setPaused(pause);
    }
    public void clearQueue(){
        this.queue.clear();
    }
    public void queueSpeech(LangString string){
        if (this.current == null) this.queueTrack(new SpeechTrack(string, MessageMaker.getLang(null, this.channel)));
        else this.speeches.add(string);
    }
    public void queueTrack(Track track){
        if (track == null) this.current = null;
        else {
            if (this.current == null) this.start(track, 0);
            else this.queue.add(track);
        }
    }
    public void interrupt(LangString langString){// must fully exist
        if (this.current == null && this.queue.isEmpty()) {
            this.queueTrack(new SpeechTrack(langString, MessageMaker.getLang(null, this.channel)));
            return;
        }
        if (this.paused != null){
            this.interups.add(langString);
            return;
        }
        this.pausePosition = this.currentTime();
        this.paused = this.current;
        this.interups.add(langString);
        this.lavaPlayer.stopTrack();
    }
    public void setPlaylistOn(boolean on){
        ConfigHandler.setSetting(QueueTrackOnlyConfig.class, this.channel.getGuild(), on);
    }
    private void start(Track track, int position){
        this.current = track;
        this.skipSet.clear();
        this.lavaPlayer.setPaused(true);
        this.lavaPlayer.startTrack(this.current.getTrack(), true);
        if (position != 0) this.lavaPlayer.getPlayingTrack().setPosition(position);
        this.lavaPlayer.setPaused(false);
        ConfigHandler.changeSetting(PlayCountConfig.class, track, integer -> integer + 1);
    }
    public void seek(long time) {
        this.lavaPlayer.getPlayingTrack().setPosition(time);
    }
    public int skipTrack() {
        if (lastSkip >= System.currentTimeMillis() - 10) throw new DevelopmentException("Sorry, skips are rate-limited at 1 per 10 seconds right now");
        this.lastSkip = System.currentTimeMillis();
        int size = this.queue.size() - 1;
        this.loop = false;
        this.lavaPlayer.stopTrack();
        return size;
    }
    public void onFinish(){
        if (this.current == null) return;
        ConfigHandler.changeSetting(ListenedCountConfig.class, this.current, integer -> integer + validListeners(this.channel));
        if (!this.interups.isEmpty()) {
            this.start(new SpeechTrack(this.interups.remove(0), MessageMaker.getLang(null, this.channel)), 0);
            return;
        }
        if (this.leaveAfterThis){
            this.leave();
            return;
        }
        if (this.loop) this.lavaPlayer.playTrack(this.current.getTrack());
        else if (!this.speeches.isEmpty()){
            this.start(new SpeechTrack(this.speeches.remove(0), MessageMaker.getLang(null, this.channel)), 0);
        }else if (this.paused != null){
            this.start(this.paused, (int) this.pausePosition);
            this.paused = null;
        }else if (this.getNext(false) != null){
            this.start(this.getNext(true), 0);
            this.next = null;
        }else this.current = null;
    }
    public Track getNext(boolean take){
        if (this.loop) return this.current;
        if (!this.queue.isEmpty()) return take ? this.queue.remove(0) : this.queue.get(0);
        if (this.next != null) return this.next;
        if (!ConfigHandler.getSetting(QueueTrackOnlyConfig.class, this.channel.getGuild())) return (this.next = ConfigHandler.getSetting(GuildActivePlaylistConfig.class, this.channel.getGuild()).getNext(this.channel.getGuild()));
        return this.next;
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
        AudioTrack track = this.lavaPlayer.getPlayingTrack();
        return track == null ? 0 : track.getPosition();
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
        if (this.skipSet.size() / (float) validListeners(this.channel) >= ConfigHandler.getSetting(SkipPercentConfig.class, this.channel.getGuild()) / 100F){
            this.skipTrack();
        }
    }
    public Guild getGuild() {
        return this.channel.getGuild();
    }
    public void setVolume(int val){
        ConfigHandler.setSetting(VolumeConfig.class, this.getGuild(), this.lavaPlayer.getVolume());
        this.lavaPlayer.setVolume(val);
    }
    public int getVolume(){
        return this.lavaPlayer.getVolume();
    }
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        this.onFinish();
    }
    @EventListener
    public static void handle(DiscordVoiceLeave event){
        GuildAudioManager manager = MAP.get(event.getGuild().getID());
        if (manager == null) return;
        if (!hasValidListeners(event.getChannel())) {
            manager.leaving = true;
            manager.leave();
        }
        manager.skipSet.remove(event.getUser());
        manager.checkSkip();
        manager.leave();
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
    public static int validListeners(VoiceChannel channel){
        return (int) channel.getConnectedUsers().stream().filter(user -> BotRole.USER.hasRequiredRole(user, null)).filter(user -> {
            IVoiceState voiceState = user.user().getVoiceStateForGuild(channel.getGuild().guild());
            return !(voiceState.isDeafened() || voiceState.isSelfDeafened());
        }).count();
    }
    public static boolean hasValidListeners(VoiceChannel channel){
        return validListeners(channel) > 0;
    }
}
