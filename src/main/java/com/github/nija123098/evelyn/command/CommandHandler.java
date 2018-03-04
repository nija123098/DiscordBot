package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.command.configs.UnknownReactionConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.config.configs.user.VoicePrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageEditEvent;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.nija123098.evelyn.exception.BotException;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.exception.GhostException;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import com.github.nija123098.evelyn.template.CustomCommandHandler;
import com.github.nija123098.evelyn.util.*;
import javafx.util.Pair;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Handles all command registration and invocation.
 *
 * @author nija123098
 * @since 1.0.0
 * @see AbstractCommand
 */
public class CommandHandler {
    private static final Map<Guild, Map<User, Set<Channel>>> NO_RESPONSE_LOCATION = new ConcurrentHashMap<>();
    private static final Map<Class<? extends AbstractCommand>, AbstractCommand> CLASS_MAP;
    private static final Map<String, AbstractCommand> REACTION_COMMAND_MAP;
    private static final Map<String, AbstractCommand> EXACT_COMMAND_MAP;
    private static final Map<String, Object> COMMANDS_MAP;
    public static final String UNKNOWN_COMMAND_EMOTICON = "grey_question", EXCEPTION_FOR_METHOD = "exclamation";
    private static final List<String> OPEN_EDIT_MESSAGES = new MemoryManagementService.ManagedList<>(30000);
    public static final AtomicReference<String> MENTION = new AtomicReference<>(), MENTION_NICK = new AtomicReference<>();
    static {
        Map<Class<? extends AbstractCommand>, Set<AbstractCommand>> typeMap = new HashMap<>();
        Set<Class<? extends AbstractCommand>> classes = new Reflections(Launcher.BASE_PACKAGE).getSubTypesOf(AbstractCommand.class);
        classes.removeIf(clazz -> Modifier.isAbstract(clazz.getModifiers()));
        CLASS_MAP = new HashMap<>(classes.size() + 10, 1);
        Set<Class<? extends AbstractCommand>> failed = new HashSet<>();
        classes.forEach(clazz -> {
            AbstractCommand command = null;
            try {
                command = clazz.newInstance();
            } catch (InstantiationException e) {
                Log.log("Exception attempting to initialize command: " + clazz.getName(), e);
            } catch (IllegalAccessException e) {
                Log.log("Malformed root command: " + clazz.getName(), e);
            } catch (DevelopmentException e) {
                Log.log("DevelopmentException while initializing command: " + clazz.getName(), e);
            } catch (RuntimeException e){
                Log.log("Exception while initializing command: " + clazz.getName(), e);
            }
            if (command == null || !clazz.isInstance(command)) {
                failed.add(clazz);
            } else {
                CLASS_MAP.put(clazz, command);
                typeMap.computeIfAbsent(command.getSuperCommandType(), s -> new HashSet<>()).add(command);
            }
        });
        AtomicBoolean modified = new AtomicBoolean(true);
        while (true) {
            modified.set(false);
            typeMap.forEach((clazz, commands) -> {
                if (failed.contains(clazz)) {
                    modified.set(true);
                    commands.forEach(command -> failed.add(command.getClass()));
                }
            });
            if (!modified.get()) break;
            failed.forEach(typeMap::remove);
        }
        failed.forEach(CLASS_MAP::remove);
        COMMANDS_MAP = new HashMap<>(CLASS_MAP.size(), 1);
        EXACT_COMMAND_MAP = new HashMap<>(CLASS_MAP.size(), 1);
        REACTION_COMMAND_MAP = new HashMap<>(CLASS_MAP.size() / 2, 1);
        typeMap.get(null).forEach(command -> load(command, typeMap));
        // alias loading
        typeMap.values().forEach(commands -> commands.forEach(command -> command.getEmoticonAliases().forEach(s -> {
            String chars = EmoticonHelper.getChars(s, false);
            REACTION_COMMAND_MAP.put(chars, command);
        })));
        CLASS_MAP.values().forEach(command -> command.getNames().forEach(s -> {
            if (s == null) return;
            String[] strings = s.split(" ");
            Map map = COMMANDS_MAP;
            for (String string : strings) map = (Map<String, Object>) map.computeIfAbsent(string, st -> new HashMap(strings.length + 2, 1));
            map.put("", command);
            EXACT_COMMAND_MAP.put(s, command);
        }));
        EventDistributor.register(CommandHandler.class);
        Launcher.registerStartup(() -> {
            MENTION.set(DiscordClient.getOurUser().mention(false));
            MENTION_NICK.set(DiscordClient.getOurUser().mention(true));
        });
    }

    /**
     * Loads a command hierarchically as determined by pre-sorted maps.
     *
     * @param superCommand the command to load it and it's sub-commands.
     * @param typeMap the map sorted so the type of the super-command
     *                is the key and the value is a set of sub-command objects.
     */
    private static void load(AbstractCommand superCommand, Map<Class<? extends AbstractCommand>, Set<AbstractCommand>> typeMap){
        superCommand.load();
        if (typeMap.containsKey(superCommand.getClass())) typeMap.get(superCommand.getClass()).forEach(command -> load(command, typeMap));
    }

    /**
     * Forces the initialization of this class.
     */
    public static void initialize(){
        Log.log(LogColor.blue("Command Handler initialized.") + LogColor.yellow(" Implementing communism."));
    }

    /**
     * Returns a set of all command instances.
     *
     * @return returns a set of all command instances.
     */
    public static Set<? extends AbstractCommand> getCommands(){
        return new HashSet<>(CLASS_MAP.values());
    }

    /**
     * Gets the command instance by the class type.
     *
     * @param commandType the command type.
     * @return the command singleton of the given type.
     */
    public static AbstractCommand getCommand(Class<? extends AbstractCommand> commandType){
        return CLASS_MAP.get(commandType);
    }

    /**
     * The method to get a command from a reaction name.
     *
     * @param reactionName the {@link Reaction} specified by name.
     * @return the abstract command whose ailiase is the reaction chars.
     */
    public static AbstractCommand getReactionCommand(String reactionName){
        return REACTION_COMMAND_MAP.get(EmoticonHelper.getChars(reactionName, false));
    }

    /**
     * Gets the command for the name given expecting no arguments.
     *
     * @param name the exact name of the command.
     * @return the exactly specified command.
     */
    public static AbstractCommand getCommand(String name){
        return EXACT_COMMAND_MAP.get(name);
    }

    /**
     * Gets the command and its arguments from a sting.
     *
     * @param in the string to derive the command
     *           and command parameters from.
     * @return the command and parameters for that command.
     */
    public static Pair<AbstractCommand, String> getMessageCommand(String in){// make return Pair<AbstractCommand, Integer>
        String[] strings = FormatHelper.reduceRepeats(in, ' ').split(" ");
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].toLowerCase();
        }
        AbstractCommand command = null;
        int index = -1;
        Map<String, Object> map = COMMANDS_MAP;
        for (int i = 0; true; ++i) {
            AbstractCommand com = (AbstractCommand) map.get("");
            if (com != null){
                command = com;
                index = i;
            }
            if (i == strings.length){
                break;
            }
            map = (Map<String, Object>) map.get(strings[i]);
            if (map == null){
                break;
            }
        }
        if (index == -1){
            return null;
        }
        for (int i = 0; i < index; ++i) {
            in = FormatHelper.trimFront(in.substring(strings[i].length()));
        }
        return new Pair<>(command, in);
    }

    /**
     * The method to attempt to run a command.
     *
     * @param string the command user args or reacted message content.
     * @param user the invoking user.
     * @param message the message reacted to or sent to invoke the command.
     * @param reaction the reaction that invoked this command, if applicable.
     * @return true if there is a command invoked, null if chat is invoked, false otherwise.
     */
    public static Boolean attemptInvocation(String string, User user, Message message, Reaction reaction){
        if (reaction != null) string = string == null ? "" : string;
        else if ((string == null || string.isEmpty())) return false;
        boolean mayChat = false;
        AbstractCommand command;
        if (message.getChannel().isPrivate()){
            if (string.startsWith(MENTION_NICK.get())){
                mayChat = true;
                string = string.substring(MENTION_NICK.get().length());
            }else if (string.startsWith(MENTION.get())){
                mayChat = true;
                string = string.substring(MENTION.get().length());
            } else {
                String original = string;
                while (!string.isEmpty() && !Character.isLetterOrDigit(string.charAt(0))) {
                    mayChat = false;
                    string = string.substring(1);
                }
                if (string.isEmpty()) string = original;
            }
            if (string.toLowerCase().startsWith("evelyn")) string = string.substring(5);
        }else{
            String pref = ConfigHandler.getSetting(GuildPrefixConfig.class, message.getGuild());
            if (string.startsWith(pref)) string = string.substring(pref.length());
            else{
                String split = string.split(" ")[0];
                if ((command = REACTION_COMMAND_MAP.get(split)) != null){
                    Pair<AbstractCommand, String> pair = getMessageCommand(command.getName() + " " + FormatHelper.trimFront(string.substring(split.length())));
                    if (pair != null){
                        command = pair.getKey();
                        string = pair.getValue();
                    }
                    try {
                        if (command.hasPermission(user, message.getChannel()) && command.checkCoolDown(message.getChannel(), user) && command.interpretSuccess(command.invoke(user, message.getShard(), message.getChannel(), message.getGuild(), message, reaction, string))){
                            command.invoked(message.getChannel(), user, message);
                            return true;
                        }
                    }catch(Exception ignored){}
                    return false;
                }else if (string.toLowerCase().startsWith("@evelyn")) string = string.substring(6);
                else if (DiscordClient.getOurUser().getNickname(message.getGuild()) != null && string.toLowerCase().startsWith("@" + DiscordClient.getOurUser().getNickname(message.getGuild()).toLowerCase())){
                    string = string.substring(1 + DiscordClient.getOurUser().getNickname(message.getGuild()).length());
                }else if (string.startsWith(MENTION.get())) {
                    string = string.substring(MENTION.get().length());
                    mayChat = true;
                }else if (string.startsWith(MENTION_NICK.get())) {
                    string = string.substring(MENTION_NICK.get().length());
                    mayChat = true;
                }else if (reaction == null) return false;
            }
        }
        string = FormatHelper.trimFront(string);
        if ((string.isEmpty() || !Character.isLetterOrDigit(string.charAt(0))) && reaction == null) return false;
        Pair<AbstractCommand, String> pair = reaction == null ? getMessageCommand(string) : ((command = getReactionCommand(reaction.getName())) == null ? null : new Pair<>(command, null));
        if (pair == null && REACTION_COMMAND_MAP.containsKey(string)) pair = new Pair<>(REACTION_COMMAND_MAP.get(string), "");
        if (pair != null){
            if (!pair.getKey().useReactions() && reaction != null) return false;
            if (!message.getChannel().getModifiedPermissions(DiscordClient.getOurUser()).contains(DiscordPermission.SEND_MESSAGES) && NO_RESPONSE_LOCATION.computeIfAbsent(message.getChannel().getGuild(), guild -> new ConcurrentHashMap<>()).computeIfAbsent(message.getAuthor(), u -> new HashSet<>()).add(message.getChannel()) && !message.getChannel().isPrivate() && message.getGuild().getUsers().stream().filter(User::isBot).count() < 6){
                new MessageMaker(message.getAuthor()).append("I can't send a command there and I won't tell you again!").send();
            }
            command = pair.getKey();
            Reaction r = message.getReactionByName(UNKNOWN_COMMAND_EMOTICON);
            if (r != null){
                message.removeReaction(r);
            }
            if (!command.hasPermission(user, message.getChannel())){
                if (reaction == null){
                    new MessageMaker(user, message).append("You do not have permission to use that command here.").withDeleteDelay(3_000L).send();
                }
                return false;
            }
            if (!command.checkCoolDown(message.getChannel(), user)){
                if (reaction == null){
                    new MessageMaker(user, message).append("You can not use that command so soon.").send();
                }
                return false;
            }
            try {
                boolean invoked = false;
                if (command.interpretSuccess(command.invoke(user, message.getShard(), message.getChannel(), message.getGuild(), message, reaction, pair.getValue()))){
                    invoked = true;
                    command.invoked(message.getChannel(), user, message);
                }
                if (message.getReactionByName(EXCEPTION_FOR_METHOD) != null){
                    message.removeReactionByName(EXCEPTION_FOR_METHOD);
                }
                return invoked;
            } catch (BotException e){
                e.makeMessage(message.getChannel()).send();
                message.addReactionByName(EXCEPTION_FOR_METHOD);
            } catch (Exception e) {
                new DevelopmentException(e).makeMessage(message.getChannel()).send();
            }
        }else if (reaction == null && !message.getChannel().isPrivate() && CustomCommandHandler.handle(message.getGuild(), message.getAuthor(), message.getChannel().getShard(), message.getChannel(), message, string)) return true;
        else if (reaction == null){
            if (mayChat) return null;
            if (FormatHelper.filtering(string, Character::isLetter).isEmpty() || (!message.getChannel().isPrivate() && !ConfigHandler.getSetting(UnknownReactionConfig.class, message.getGuild()))) return false;
            message.addReactionByName(UNKNOWN_COMMAND_EMOTICON);
            OPEN_EDIT_MESSAGES.add(message.getID());
        }
        return false;
    }

    /**
     * Attempts to execute a command invoked from a voice channel.
     *
     * @param s the words used to invoke the command.
     * @param user the user invoking the command.
     * @param manager the audio manager that got the command.
     * @return if a command was invoked and succeeded.
     */
    public static boolean attemptInvocation(String s, User user, GuildAudioManager manager){
        if (s == null || s.isEmpty()) return false;
        String prefix = ConfigHandler.getSetting(VoicePrefixConfig.class, user);
        boolean prefixFound = true;
        if (!s.startsWith(prefix)) prefixFound = false;
        else s = FormatHelper.trimFront(s.substring(prefix.length()));
        Pair<AbstractCommand, String> pair = getMessageCommand(s);
        if (pair == null) manager.interrupt(new LangString(true, "Invalid command " + s));
        else {
            if (pair.getKey().prefixRequired() && !prefixFound) return false;
            VoiceChannel channel = manager.voiceChannel();
            if (!pair.getKey().hasPermission(user, channel)){
                manager.interrupt(new LangString(true, "You do not have permission to use that command"));
                return false;
            }
            if (!pair.getKey().checkCoolDown(channel, user)){
                manager.interrupt(new LangString(true, "You can not use that command so soon"));
                return false;
            }
            try {
                if (pair.getKey().interpretSuccess(pair.getKey().invoke(user, manager.getGuild().getShard(), manager.voiceChannel(), manager.getGuild(), null, null, pair.getValue()))){
                    pair.getKey().invoked(channel, user, null);
                    return true;
                }
            } catch (BotException e){
                e.makeMessage(channel).send();
            } catch (Exception e) {
                new DevelopmentException(e).makeMessage(channel).send();
            }
        }
        return false;
    }

    /**
     * The monitoring for arg based commands.
     *
     * @param event the monitored event.
     * @return true if a event was invoked, false if
     * {@link com.github.nija123098.evelyn.chatbot.ChatBot}
     * did not find the message acceptable, null otherwise.
     */
    public static Boolean handle(DiscordMessageReceived event){
        try{return attemptInvocation(event.getMessage().getContent(), event.getAuthor(), event.getMessage(), null);// new MessageMaker(event.getMessage()).append(ChatBot.getChatBot(event.getChannel()).think(event.getMessage().getContent())).send();
        } catch (Exception e){
            if (GhostException.isGhostCaused(e)) return true;
            throw e;
        }
    }

    /**
     * Listens for reaction aliased command.
     *
     * @param event the monitored event.
     */
    @EventListener
    public static void handle(DiscordReactionEvent event){
        if (!event.getUser().isBot() && Launcher.isReady() && (!event.getMessage().getAuthor().isBot() || event.getMessage().getAuthor().equals(DiscordClient.getOurUser()))){
            attemptInvocation(event.getMessage().getContent(), event.getUser(), event.getMessage(), event.getReaction());
        }
    }

    /**
     * Listens for a reattempt at a command through editing.
     *
     * @param event the monitored event.
     */
    @EventListener
    public static void handle(DiscordMessageEditEvent event){
        if (!event.getAuthor().isBot() && OPEN_EDIT_MESSAGES.contains(event.getOldMessage().getID())){
            if (Care.lessBoolean(attemptInvocation(event.getOldMessage().getContent(), event.getAuthor(), event.getOldMessage(), null))){
                OPEN_EDIT_MESSAGES.remove(event.getOldMessage().getID());
            }
        }
    }
}
