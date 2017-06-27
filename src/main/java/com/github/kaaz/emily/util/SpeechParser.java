package com.github.kaaz.emily.util;

import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordSpeakingEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordVoiceJoin;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordVoiceLeave;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.perms.BotRole;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import sx.blah.discord.handle.audio.IAudioReceiver;
import sx.blah.discord.handle.obj.IUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class SpeechParser implements IAudioReceiver {
    private static final Map<GuildAudioManager, Map<User, SpeechParser>> PARSER_MAP = new ConcurrentHashMap<>();
    private static final Set<GuildAudioManager> ACTIVE = new HashSet<>();
    private static final StreamSpeechRecognizer RECOGNIZER;
    static {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        StreamSpeechRecognizer recognizer = null;
        try {
            recognizer = new StreamSpeechRecognizer(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RECOGNIZER = recognizer;
        EventDistributor.register(SpeechParser.class);
    }
    public static void registerParser(User user, GuildAudioManager manager){
        PARSER_MAP.computeIfAbsent(manager, c -> new ConcurrentHashMap<>()).computeIfAbsent(user, u -> new SpeechParser(user, manager));
    }
    public static void unregisterParser(User user, GuildAudioManager manager){
        manager.getGuild().guild().getAudioManager().unsubscribeReceiver(PARSER_MAP.get(manager).remove(user));
    }
    public static void unregister(GuildAudioManager manager){
        PARSER_MAP.get(manager).forEach((user1, speechParser) -> unregisterParser(user1, manager));
    }
    public static void activeate(GuildAudioManager manager, boolean activate){
        if (ACTIVE.contains(manager) == activate) return;
        if (activate) ACTIVE.add(manager);
        else ACTIVE.remove(manager);
        PARSER_MAP.get(manager).forEach((u, parser) -> parser.activate(activate));
    }
    private final User user;
    private final GuildAudioManager audioManager;
    private SpeechParser(User user, GuildAudioManager audioManager) {
        this.user = user;
        this.audioManager = audioManager;
    }
    private final List<Byte> bytes = new ArrayList<>();
    @Override
    public synchronized void receive(byte[] audio, IUser user, char sequence, int timestamp) {
        for (byte anAudio : audio) this.bytes.add(anAudio);
    }
    private synchronized void onStart(){
    }
    private synchronized void onStop(){
        process(scan(alter(write(FileHelper.getTempFile("voiceparsing", "pcm")))));// this might need to be more condiment
        this.bytes.clear();
    }
    private void activate(boolean activate){
        if (activate) this.audioManager.getGuild().guild().getAudioManager().subscribeReceiver(this);
        else this.audioManager.getGuild().guild().getAudioManager().unsubscribeReceiver(this);
    }
    private File write(File file){
        byte[] bytes = new byte[this.bytes.size()];
        for (int i = 0; i < this.bytes.size(); i++) {
            bytes[i] = this.bytes.get(i);
        }
        try {
            if (!file.exists()) file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    private static File alter(File file){
        File ret = new File(file.getPath().replace(".pcm", ".wav"));
        if (ret.exists()) ret.delete();
        List<String> command = new ArrayList<>();
        command.add(BotConfig.FFM_PEG_PATH);
        command.add("-f");// depth
        command.add("s16be");
        command.add("-ar");
        command.add("48k");// sample rate
        command.add("-ac");
        command.add("2");// channels
        command.add("-i");
        command.add(file.getPath());
        command.add("-ac");
        command.add("1");// to channels
        command.add("-ar");
        command.add("16k");// to sample rate
        command.add(ret.getPath());
        Process process = null;
        try {
            process = new ProcessBuilder(command).start();
            new StreamGobler(process.getInputStream(), System.out).start();
            new StreamGobler(process.getErrorStream(), System.err).start();
            process.waitFor(10, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (process != null) process.destroyForcibly();
        }
        file.delete();
        return ret;
    }
    private static String scan(File file){
        String ret = null;
        synchronized (SpeechParser.class){
            try {
                RECOGNIZER.startRecognition(new FileInputStream(file));
                SpeechResult result = RECOGNIZER.getResult();
                if (result != null) ret = result.getHypothesis();
                RECOGNIZER.stopRecognition();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        file.delete();
        return ret;
    }
    private void process(String s){
        if (s == null || s.isEmpty()) return;
        CommandHandler.attemptInvocation(s, this.user, this.audioManager);
    }
    private static final ExecutorService EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(1, (ThreadFactory) Thread::new);
    @EventListener
    public static void handle(DiscordSpeakingEvent event){
        EXECUTOR_SERVICE.submit(() -> {
            SpeechParser parser = PARSER_MAP.computeIfAbsent(GuildAudioManager.getManager(event.getGuild()), c -> new ConcurrentHashMap<>()).get(event.getUser());
            if (parser != null) {
                if (event.isSpeaking()) parser.onStart();
                else parser.onStop();
            }
        });
    }
    @EventListener
    public static void handle(DiscordVoiceJoin event){
        if (BotRole.SUPPORTER.hasRole(event.getUser(), null)) {
            GuildAudioManager manager = GuildAudioManager.getManager(event.getChannel(), false);
            if (manager != null) activeate(manager, true);
        }
    }
    @EventListener
    public static void handle(DiscordVoiceLeave event){
        for (User user : event.getChannel().getConnectedUsers()) if (BotRole.SUPPORTER.hasRole(user, null)) return;
        GuildAudioManager manager = GuildAudioManager.getManager(event.getChannel(), false);
        if (manager != null) activeate(manager, false);
    }
}
