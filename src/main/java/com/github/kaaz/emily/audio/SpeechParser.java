package com.github.kaaz.emily.audio;

import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordSpeakingEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordVoiceJoin;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordVoiceLeave;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.*;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import sx.blah.discord.handle.audio.IAudioReceiver;
import sx.blah.discord.handle.obj.IUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class SpeechParser implements IAudioReceiver {
    private static final InitBuffer<StreamSpeechRecognizer> SPEECH_RECOGNIZER_BUFFER;
    private static final Map<Guild, Map<User, SpeechParser>> PARSER_MAP;
    static {
        if (BotConfig.VOICE_COMMANDS_ENABLED){
            PARSER_MAP = new ConcurrentHashMap<>();
            Configuration configuration = new Configuration();
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
            configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
            SPEECH_RECOGNIZER_BUFFER = new InitBuffer<>(2, () -> {
                try{return new StreamSpeechRecognizer(configuration);
                } catch (IOException e){
                    Log.log("IOException while making speech recognizer instance", e);
                    return null;
                }
            });
            EventDistributor.register(SpeechParser.class);
        }else{
            SPEECH_RECOGNIZER_BUFFER = null;
            PARSER_MAP = null;
        }
    }
    public static void init(){}
    private boolean lon;
    private final User user;
    private final GuildAudioManager audioManager;
    private final List<Byte> bytes = new ArrayList<>();
    private SpeechParser(User user, GuildAudioManager audioManager) {
        this.user = user;
        this.audioManager = audioManager;
        this.audioManager.getGuild().guild().getAudioManager().subscribeReceiver(this);
    }
    @Override
    public synchronized void receive(byte[] audio, IUser user, char sequence, int timestamp) {
        if (this.lon) return;
        if (this.bytes.size() > 100_000){
            this.bytes.clear();
            this.lon = true;
            return;
        }
        for (byte anAudio : audio) this.bytes.add(anAudio);
    }
    private synchronized void onStop(){
        if (this.lon) {
            this.lon = false;
            return;
        }
        if (this.bytes.size() > 2000) {
            process(scan(alter(write(FileHelper.getTempFile("voiceparsing", "pcm")))));// this might need to be more condiment
        }
        this.bytes.clear();
    }
    private void close(){
        this.audioManager.getGuild().guild().getAudioManager().unsubscribeReceiver(this);
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
        AtomicReference<String> reference = new AtomicReference<>();
        SPEECH_RECOGNIZER_BUFFER.borrow(recognizer -> {
            try {
                recognizer.startRecognition(new FileInputStream(file));
                SpeechResult result = recognizer.getResult();
                if (result != null) reference.set(result.getHypothesis());
                recognizer.stopRecognition();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ScheduleService.schedule(file.length() * 5, () -> {
                try{Files.delete(file.toPath());
                }catch(IOException e) {e.printStackTrace();}
            });
        });
        return reference.get();
    }
    private void process(String s){
        if (s == null || s.isEmpty()) return;
        CommandHandler.attemptInvocation(s, this.user, this.audioManager);
    }
    @EventListener
    public static void handle(DiscordSpeakingEvent event){
        if (!PARSER_MAP.containsKey(event.getGuild())) return;
        SpeechParser parser = PARSER_MAP.computeIfAbsent(event.getGuild(), c -> new ConcurrentHashMap<>()).get(event.getUser());
        if (parser != null && !event.isSpeaking()) ThreadProvider.sub(parser::onStop);
    }
    @EventListener
    public static void handle(DiscordVoiceJoin event){
        if (!event.getChannel().isConnected()) return;
        if (DiscordClient.getOurUser().equals(event.getUser())){
            boolean found = false;
            List<User> connected = event.getChannel().getConnectedUsers();
            for (User user : connected){
                if (user.isBot()) continue;
                if (BotRole.SUPPORTER.hasRole(user, null)){
                    found = true;
                    break;
                }
            }
            if (!found) return;
            register(event.getGuild(), event.getChannel().getConnectedUsers());
        } else {
            if (event.getUser().isBot()) return;
            if (PARSER_MAP.containsKey(event.getGuild())) PARSER_MAP.get(event.getGuild()).put(event.getUser(), new SpeechParser(event.getUser(), GuildAudioManager.getManager(event.getGuild())));
            else if (BotRole.SUPPORTER.hasRole(event.getUser(), null)) register(event.getGuild(), event.getChannel().getConnectedUsers());
        }
    }
    private static void register(Guild guild, List<User> users){
        GuildAudioManager manager = GuildAudioManager.getManager(guild);
        Map<User, SpeechParser> map = new HashMap<>();
        users.stream().filter(user -> !user.isBot()).forEach(u -> map.put(u, new SpeechParser(u, manager)));
        PARSER_MAP.put(guild, map);
    }
    @EventListener
    public static void handle(DiscordVoiceLeave event){
        if (!PARSER_MAP.containsKey(event.getGuild())) return;
        if (DiscordClient.getOurUser().equals(event.getUser())){
            PARSER_MAP.remove(event.getGuild()).values().forEach(SpeechParser::close);
        } else {
            if (event.getUser().isBot()) return;
            for (User user : event.getChannel().getConnectedUsers()) if (BotRole.SUPPORTER.hasRole(user, null)) return;
            PARSER_MAP.remove(event.getGuild()).values().forEach(SpeechParser::close);
        }
    }
}
