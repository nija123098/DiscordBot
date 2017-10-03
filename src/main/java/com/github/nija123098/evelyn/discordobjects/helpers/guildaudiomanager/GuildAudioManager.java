package com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager;

import com.github.nija123098.evelyn.audio.SpeechTrack;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.audio.commands.current.CurrentCommand;
import com.github.nija123098.evelyn.audio.configs.GreetingsVoiceConfig;
import com.github.nija123098.evelyn.audio.configs.guild.QueueTrackOnlyConfig;
import com.github.nija123098.evelyn.audio.configs.guild.SkipPercentConfig;
import com.github.nija123098.evelyn.audio.configs.track.PlayCountConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.moderation.logging.MusicChannelConfig;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildActivePlaylistConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordVoiceLeave;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.GhostException;
import com.github.nija123098.evelyn.launcher.BotConfig;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.Care;
import com.github.nija123098.evelyn.util.LangString;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.ThreadProvider;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The manager for a single guild for a single voice connection.
 * This is trashed after the bot leaves the voice channel this managed.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class GuildAudioManager extends AudioEventAdapter{
    private static final int BUFFER_SIZE = 40;
    private static final Map<String, GuildAudioManager> MAP = new ConcurrentHashMap<>();
    public static final DefaultAudioPlayerManager PLAYER_MANAGER = new DefaultAudioPlayerManager();
    static {
        PLAYER_MANAGER.registerSourceManager(new YoutubeAudioSourceManager(false));
        PLAYER_MANAGER.registerSourceManager(new SoundCloudAudioSourceManager(false));
        PLAYER_MANAGER.registerSourceManager(new LocalAudioSourceManager());
        AtomicInteger integer = new AtomicInteger();
        AbstractConfig<List<Track>, VoiceChannel> config = ConfigHandler.getConfig(PlayQueueConfig.class);
        Launcher.registerStartup(() -> config.getNonDefaultSettings().forEach((channel, tracks) -> ScheduleService.schedule(integer.getAndIncrement() * 1000 + 5_000, () -> {
            config.reset(channel);
            if (!hasValidListeners(channel)) return;
            GuildAudioManager manager = getManager(channel);
            manager.queueTrack(tracks.remove(0));
            manager.queue.addAll(tracks);
        })));
        PLAYER_MANAGER.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
        PLAYER_MANAGER.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
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

    /**
     * Gets a instance based on the {@link VoiceChannel} and
     * if a manager should be made if no such manager exists.
     *
     * @param channel the channel to get a manager for
     * @param make if a manager should be made if one does not exist for this guild
     * @return a manager instance for the given channel
     * @throws ArgumentException if the bot would have no valid listeners or if a manager exists elsewhere in the guild
     */
    public static GuildAudioManager getManager(VoiceChannel channel, boolean make){
        if (channel == null) return null;// this might not happen anymore
        if (BotConfig.GHOST_MODE) throw new GhostException();
        GuildAudioManager current = getManager(channel.getGuild());
        if (current != null) {
            if (!current.voiceChannel().isConnected()) MAP.replace(channel.getID(), new GuildAudioManager(channel));
            if (!current.channel.equals(channel)) {
                if (make) throw new ArgumentException("You must be in the voice channel with me to use that command");
                else return null;
            }
            return current;
        }
        if (make) {
            if (!hasValidListeners(channel)) throw new ArgumentException("Someone has to be able to hear me in that voice channel");
            AtomicReference<VoiceChannel> reference = new AtomicReference<>(channel);
            return MAP.computeIfAbsent(channel.getGuild().getID(), s -> new GuildAudioManager(reference.get()));
        }
        return null;
    }

    /**
     * Gets a instance based on the {@link VoiceChannel}
     *
     * @param channel the channel to get a manager for
     * @return a manager instance for the given channel
     * @throws ArgumentException if the bot would have no valid listeners or if a manager exists elsewhere in the guild
     */
    public static GuildAudioManager getManager(VoiceChannel channel){
        return getManager(channel, true);
    }

    /**
     * Gets a manager instance if one exists for the given guild.
     *
     * @param guild the guild to get a manager for if one exists
     * @return a manager instance if one exists for the guild or null
     */
    public static GuildAudioManager getManager(Guild guild){
        return MAP.get(guild.getID());
    }
    private AudioProvider audioProvider;
    private VoiceChannel channel;
    private AudioPlayer lavaPlayer;
    private final List<Message> currentDisplays = new ArrayList<>(3);
    private final Set<User> skipSet = new ConcurrentHashSet<>();
    private final List<LangString> speeches = new CopyOnWriteArrayList<>();
    private final List<Track> queue = new CopyOnWriteArrayList<>();
    private final List<LangString> interups = new CopyOnWriteArrayList<>();
    private Track current, paused, next;
    private long pausePosition;
    private boolean leaveAfterThis, loop, leaving, skipped, swapping, pause, destroyed;
    private GuildAudioManager(VoiceChannel channel) {
        this.channel = channel;
        this.lavaPlayer = PLAYER_MANAGER.createPlayer();
        this.lavaPlayer.addListener(this);
        this.audioProvider = new AudioProvider(this);
        this.channel.channel().getGuild().getAudioManager().setAudioProvider(this.audioProvider);
        this.setVolume(ConfigHandler.getSetting(VolumeConfig.class, channel.getGuild()));
        this.channel.join();
        if (ConfigHandler.getSetting(GreetingsVoiceConfig.class, channel.getGuild())) this.start(new SpeechTrack(new LangString(true, "Hello"), MessageMaker.getLang(null, this.channel)), 0);
    }
    public void leave(){
        if (this.leaving || !hasValidListeners(this.channel) || ConfigHandler.getSetting(GreetingsVoiceConfig.class, channel.getGuild())){
            this.leaving = true;
            MAP.remove(this.channel.getGuild().getID());
            this.channel.leave();
            this.audioProvider.run.set(false);
            this.lavaPlayer.setPaused(true);
            this.lavaPlayer.destroy();
            this.destroyed = true;
        }else{
            this.audioProvider.clearBuffer();
            this.clearQueue();
            this.interups.clear();
            this.speeches.clear();
            if (ConfigHandler.getSetting(GreetingsVoiceConfig.class, channel.getGuild())) this.interrupt(new LangString(true, "Goodbye"));
            if (this.current != null) this.skipTrack();
            this.loop = false;
            this.leaveAfterThis = true;
            this.leaving = true;
        }
    }

    /**
     * Pauses of un-pauses the current track.
     *
     * @param pause if the current track should be paused
     */
    public void pause(boolean pause){
        this.lavaPlayer.setPaused(pause);
        this.pause = pause;
    }

    /**
     * Removes all songs from the queue, not the currently playing song.
     */
    public void clearQueue(){
        this.queue.clear();
    }

    /**
     * Queues a new speech which will be played now
     * or if a track is currently playing afterword.
     *
     * @param string the thing the bot should say
     */
    public void queueSpeech(LangString string){
        if (this.current == null) this.queueTrack(new SpeechTrack(string, MessageMaker.getLang(null, this.channel)));
        else this.speeches.add(string);
    }

    /**
     * Queues a new speech which will be played now or
     * if a track is currently playing be placed in queue.
     *
     * @param track the track to queue or play next
     */
    public void queueTrack(Track track){
        if (track == null) this.current = null;
        else {
            if (this.current == null) this.start(track, 0);
            else this.queue.add(track);
        }
    }

    /**
     * Swaps the currently playing if it is equivalent and
     * resumes using the new provision of {@link AudioTrack}.
     *
     * @param track the track to swap if applicable
     */
    public void swap(Track track){
        if (!track.equals(this.currentTrack())) return;
        AudioTrack audioTrack = track.getAudioTrack(null);
        this.lavaPlayer.setPaused(true);
        this.swapping = true;
        audioTrack.setPosition(this.currentTime());
        this.lavaPlayer.stopTrack();
        this.lavaPlayer.startTrack(audioTrack, false);
        this.lavaPlayer.setPaused(false);
    }

    /**
     * Interrupts any currently playing music and
     * makes the bot say the translated string or
     * queues it behind any current interrupts.
     *
     * @param langString the thing to interrupt
     */
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
    private void start(Track track, int position){
        this.skipped = false;
        this.current = track;
        this.skipSet.clear();
        this.lavaPlayer.setPaused(true);
        this.lavaPlayer.startTrack(this.current.getAudioTrack(this), true);
        if (position != 0) this.lavaPlayer.getPlayingTrack().setPosition(position);
        this.lavaPlayer.setPaused(false);
        if (this.destroyed) throw new RuntimeException("Hey lava player, I destroyed you, NOW GO AWAY");
        if (!this.loop) ThreadProvider.sub(() -> {
            Track current = this.currentTrack();
            if (current instanceof SpeechTrack) return;
            Channel channel = ConfigHandler.getSetting(MusicChannelConfig.class, this.getGuild());
            if (channel == null) return;
            MessageMaker maker = new MessageMaker(channel);
            if (this.leaving) this.currentDisplays.forEach(Message::delete);
            else{
                CurrentCommand.command(this.getGuild(), maker, current);
                maker.send();
                this.currentDisplays.add(maker.sentMessage());
                if (this.currentDisplays.size() > 2 && !this.currentDisplays.isEmpty()) this.currentDisplays.remove(0).delete();
            }
        });
    }

    /**
     * Changes the time of the track to the given time.
     *
     * @param time the time to change a track to.
     */
    public void seek(long time) {
        if (time < 0 || time > this.lavaPlayer.getPlayingTrack().getInfo().length) throw new ArgumentException("The song is only " + this.lavaPlayer.getPlayingTrack().getInfo().length + " seconds long");
        this.lavaPlayer.getPlayingTrack().setPosition(time);
    }

    /**
     * Forces the skipping of the current track, regardless of the vote.
     *
     * @return the number of songs left in queue
     */
    public int skipTrack() {
        this.audioProvider.clearBuffer();
        int size = this.queue.size() - 1;
        this.loop = false;
        this.pause = false;
        this.lavaPlayer.stopTrack();
        this.skipped = true;
        return size;
    }
    private void onFinish(){
        if (this.current == null) return;
        if (this.swapping){
            this.swapping = false;
            return;
        }
        if (!this.skipped && !(this.current instanceof SpeechTrack)) ConfigHandler.changeSetting(PlayCountConfig.class, this.current, integer -> integer + validListeners(this.channel));
        if (!this.interups.isEmpty()) {
            this.start(new SpeechTrack(this.interups.remove(0), MessageMaker.getLang(null, this.channel)), 0);
            return;
        }
        if (this.leaveAfterThis){
            this.leave();
            return;
        }
        if (this.loop) this.lavaPlayer.playTrack(this.current.getAudioTrack(null));
        else if (!this.speeches.isEmpty()){
            this.start(new SpeechTrack(this.speeches.remove(0), MessageMaker.getLang(null, this.channel)), 0);
        }else if (this.paused != null){
            this.start(this.paused, (int) this.pausePosition);
            this.paused = null;
        }else if (this.getNext(false) != null){
            this.start(this.getNext(true), 0);
            this.next = null;
        }else this.current = null;
        if (this.current != null && !(this.current instanceof SpeechTrack) && this.pause){
            this.lavaPlayer.setPaused(true);
        }
    }

    /**
     * Gets the track that should play next.
     *
     * @param take if that track should be removed from the possible next play
     * @return the track to play next
     */
    public Track getNext(boolean take){
        if (this.loop) return this.current;
        if (!this.queue.isEmpty()) return take ? this.queue.remove(0) : this.queue.get(0);
        if (this.next != null) return this.next;
        if (!ConfigHandler.getSetting(QueueTrackOnlyConfig.class, this.channel.getGuild())) return (this.next = ConfigHandler.getSetting(GuildActivePlaylistConfig.class, this.channel.getGuild()).getNext(this.channel.getGuild()));
        return this.next;
    }

    /**
     * Sets the bot to leave the voice channel after the currently playing song.
     */
    public void leaveAfterThis(){
        this.leaveAfterThis = true;
    }

    /**
     * Sets if the bot should loop the currently playing {@link Track}.
     *
     * @param loop if the bot should loop the currently playing {@link Track}
     */
    public void loop(boolean loop){
        this.loop = loop;
    }

    /**
     * Gets if the bot is currently playing a {@link Track} on loop.
     *
     * @return if the bot is currently playing a {@link Track} on loop
     */
    public boolean isLooping() {
        return this.loop;
    }

    /**
     * Gets the currently playing {@link Track}.
     *
     * @return teh currently playing {@link Track}
     */
    public Track currentTrack() {
        return this.current;
    }

    /**
     * Gets the current time of the current {@link Track} in seconds.
     *
     * @return the current time of the current {@link Track} in seconds
     */
    public long currentTime(){
        AudioTrack track = this.lavaPlayer.getPlayingTrack();
        return track == null ? 0 : track.getPosition();
    }

    /**
     * Gets a list of the {@link Track}s to be played after the current in order.
     *
     * @return the {@link Track}s to be played after the current in order.
     */
    public List<Track> getQueue() {
        return this.queue;
    }

    /**
     * Gets the {@link VoiceChannel} this manager manages.
     *
     * @return the {@link VoiceChannel} this manager manages
     */
    public VoiceChannel voiceChannel() {
        return this.channel;
    }

    /**
     * Gets if the current song is paused.
     *
     * @return if the current song is paused
     */
    public boolean isPaused() {
        return this.lavaPlayer.isPaused();
    }

    /**
     * Checks the vote if the current song should be skipped and does so if it should.
     */
    private void checkSkip(){
        if (this.skipSet.size() / (float) validListeners(this.channel) >= ConfigHandler.getSetting(SkipPercentConfig.class, this.channel.getGuild()) / 100F){
            this.skipTrack();
        }
    }

    /**
     * Gets the guild this manager is managing.
     *
     * @return the guild this manager is managing
     */
    public Guild getGuild() {
        return this.channel.getGuild();
    }

    /**
     * Sets the volume the manager should play at.
     *
     * @param val the volume the manager should play at
     */
    public void setVolume(int val){
        this.lavaPlayer.setVolume((int) (val * 1.5F));
        ConfigHandler.setSetting(VolumeConfig.class, this.getGuild(), val);
    }

    /**
     * Gets the volume the manager should play at.
     *
     * @return the volume the manager should play at
     */
    public int getVolume(){// this isn't the only thing we lie to our users about
        return this.lavaPlayer.getVolume() / 3 * 2;
    }
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        this.onFinish();
    }
    @EventListener
    public static void handle(DiscordVoiceLeave event){
        GuildAudioManager manager = MAP.get(event.getGuild().getID());
        if (manager == null || !manager.channel.equals(event.getChannel())) return;
        if (!hasValidListeners(event.getChannel())) {
            manager.leaving = true;
            manager.leave();
        }
        manager.skipSet.remove(event.getUser());
        manager.checkSkip();
    }
    private static final AudioFrame NULL = new AudioFrame(0, null, 0, null);
    public class AudioProvider implements IAudioProvider {
        private final BlockingQueue<AudioFrame> frames = new LinkedBlockingQueue<>(BUFFER_SIZE);
        private final Thread queueThread;
        private final AtomicBoolean run = new AtomicBoolean(true);
        private AudioProvider(GuildAudioManager manager) {
            this.queueThread = new Thread(() -> {
                while (run.get()){
                    if (this.frames.size() < BUFFER_SIZE){
                        AudioFrame frame = manager.lavaPlayer.provide();
                        this.frames.add(frame == null ? NULL : frame);
                    }else Care.lessSleep(5);
                }
            });
            this.queueThread.setDaemon(true);
            this.queueThread.start();
        }
        @Override
        public boolean isReady() {
            return !this.frames.isEmpty() && this.queueThread.isAlive();
        }
        @Override
        public byte[] provide() {
            try {
                if (!isReady()) return null;// This is redundant, but Lavaplayer doesn't always work well either.
                return frames.take().data;
            } catch (InterruptedException e) {
                Log.log("Interrupted taking frame, ending song", e);
                return null;
            }
        }
        public void clearBuffer(){
            this.frames.clear();
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
        return (int) channel.getConnectedUsers().stream().filter(user -> !user.isBot()).filter(user -> {
            IVoiceState voiceState = user.user().getVoiceStateForGuild(channel.getGuild().guild());
            return !(voiceState.isDeafened() || voiceState.isSelfDeafened());
        }).count();
    }
    public static boolean hasValidListeners(VoiceChannel channel){
        return validListeners(channel) > 0;
    }
}
