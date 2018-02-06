package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.chatbot.ChatBot;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.discordobjects.DiscordAdapter;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
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
import org.apache.commons.io.IOUtils;
import sx.blah.discord.handle.audio.IAudioReceiver;
import sx.blah.discord.handle.obj.IUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
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
    private static final Predicate<User> ALLOW_PARSER = u -> BotRole.SUPPORTER.hasRole(u, null);
    private static final InitBuffer<StreamSpeechRecognizer> SPEECH_RECOGNIZER_BUFFER;
    private static final Map<Guild, Map<User, SpeechParser>> PARSER_MAP;
    static {
        if (ConfigProvider.BOT_SETTINGS.voiceCommandsEnabled()){
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
    private final ArrayList<Byte> bytes = new ArrayList<>();
    private SpeechParser(User user, GuildAudioManager audioManager) {
        this.user = user;
        this.audioManager = audioManager;
        this.audioManager.getGuild().guild().getAudioManager().subscribeReceiver(this);
    }

    /**
     * Receives the user's voice data and stops
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
        if (this.bytes.size() >= 1_000_000){
            System.out.println("TOO LARGE");
            this.bytes.clear();
            this.lon = true;
            return;
        }
        this.bytes.ensureCapacity(this.bytes.size() + audio.length - 1);
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
        if (this.bytes.size() > 100_000) {
            process(scan(alter(write(FileHelper.getTempFile("voiceparsing", "pcm")), this.audioManager.voiceChannel().getBitrate())));// this might need to be more condiment
        } else System.out.println("TOO SMALL");
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
     * @param bitrate the bitrate of the incoming audio.
     * @return the input file.
     */
    private static File alter(File file, int bitrate){
        File ret = new File(file.getPath().replace(".pcm", ".wav"));
        if (ret.exists()) ret.delete();
        List<String> command = new ArrayList<>();
        command.add(ConfigProvider.EXECUTABLE_FILES.ffmPeg());
        command.add("-f");// depth
        command.add("s16be");
        command.add("-ar");
        command.add("48k");// sample rate
        command.add("-ac");
        command.add("2");// channels
        command.add("-i");
        command.add(file.getPath());
        command.add("-b:a");// bit rate
        command.add(Integer.toString(bitrate));
        command.add("-ac");
        command.add("1");// to channels
        command.add("-ar");
        command.add("16k");// to sample rate
        command.add(ret.getPath());
        Process process = null;
        try {
            process = new ProcessBuilder(command).start();
            IOUtils.copy(process.getInputStream(), System.out);
            IOUtils.copy(process.getErrorStream(), System.err);
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
            ScheduleService.schedule(600_000, () -> {
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
            if (GuildAudioManager.getManager(event.getChannel(), false) == null) return;
            boolean found = false;
            for (User user : event.getChannel().getConnectedUsers()){// break as soon as match is found
                if (!user.isBot() && ALLOW_PARSER.test(user)){
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
            for (User user : event.getChannel().getConnectedUsers()) {
                if (BotRole.SUPPORTER.hasRole(user, null)) {// break as soon as match is found
                    PARSER_MAP.get(event.getGuild()).remove(event.getUser()).close();
                    return;
                }
            }
            PARSER_MAP.remove(event.getGuild()).values().forEach(SpeechParser::close);
        }
    }
}
