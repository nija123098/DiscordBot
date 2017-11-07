package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.BotConfig.ReadConfig;
import com.github.nija123098.evelyn.chatbot.ChatBot;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.discordobjects.DiscordAdapter;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordSpeakingEvent;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordVoiceJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordVoiceLeave;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.*;
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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A type for defining a speech parser for a single
 * GuildAudioManager and user combination which
 * determines what every user says individually.
 *
 * It handles voices like {@link DiscordAdapter}
 * does message processing but does not consider {@link ChatBot}.
 *
 * This class implements Discord4J's {@link IAudioReceiver}
 * to receive a single user's audio data.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class SpeechParser implements IAudioReceiver {
    private static final InitBuffer<StreamSpeechRecognizer> SPEECH_RECOGNIZER_BUFFER;
    private static final Map<Guild, Map<User, SpeechParser>> PARSER_MAP;
    static {
        if (ReadConfig.VOICE_COMMANDS_ENABLED){
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

    /**
     * Recives the user's voice data and stops
     * listening if the cumulative data becomes too long.
     *
     * @param audio the raw OPUS data sent by the user.
     * @param user the user whose data is being received.
     * @param sequence no clue what this does.
     * @param timestamp the time stamp.
     */
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

    /**
     * Clears the user's data and attempts parsing if enough data was given.
     */
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

    /**
     * De-registers the instance as a {@link IAudioReceiver}.
     */
    private void close(){
        this.audioManager.getGuild().guild().getAudioManager().unsubscribeReceiver(this);
    }

    /**
     * Writes the raw OPUS data to the given file.
     *
     * @param file the file to write the current data to.
     * @return the input file.
     */
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
            Log.log("Exception writing speech to file", e);
        }
        return file;
    }

    /**
     * Adds a WAV header to the PCM data at the file.
     *
     * @param file the file to add the WAV header for.
     * @return the input file.
     */
    private static File alter(File file){
        File ret = new File(file.getPath().replace(".pcm", ".wav"));
        if (ret.exists()) ret.delete();
        List<String> command = new ArrayList<>();
        command.add(ReadConfig.FFM_PEG_PATH);
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
            Log.log("Exception converting file to compatible stats", e);
            return null;
        } finally {
            if (process != null) process.destroyForcibly();
        }
        file.delete();
        return ret;
    }

    /**
     * Scans the given file for voice data to determine what the user said.
     *
     * @param file the file to scan voice data for.
     * @return the {@link String} representation of what the user said.
     */
    private static String scan(File file){
        AtomicReference<String> reference = new AtomicReference<>();
        SPEECH_RECOGNIZER_BUFFER.borrow(recognizer -> {
            try {
                recognizer.startRecognition(new FileInputStream(file));
                SpeechResult result = recognizer.getResult();
                if (result != null) reference.set(result.getHypothesis());
                recognizer.stopRecognition();
            } catch (IOException e) {
                Log.log("Exception scanning file for voice data", e);
            }
            ScheduleService.schedule(file.length() * 5, () -> {
                try{Files.delete(file.toPath());
                }catch(IOException e) {Log.log("Exception deleting voice data file", e);}
            });
        });
        return reference.get();
    }
    private void process(String s){
        if (s == null || s.isEmpty()) return;
        CommandHandler.attemptInvocation(s, this.user, this.audioManager);
    }

    /**
     * Assists in detecting if a {@link User}'s utterance is complete.
     *
     * @param event the speaking detection event.
     */
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
        PARSER_MAP.put(guild, users.stream().filter(user -> !user.isBot()).collect(Collectors.toMap(Function.identity(), o -> new SpeechParser(o, manager))));
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
