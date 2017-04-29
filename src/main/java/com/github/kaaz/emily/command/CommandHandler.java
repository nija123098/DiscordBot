package com.github.kaaz.emily.command;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.guild.GuildPrefixConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageEditEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceivedEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.kaaz.emily.exeption.BotException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.launcher.Reference;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Log;
import javafx.util.Pair;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Handles all command registration and invocation.
 *
 * @author nija123098
 * @since 2.0.0
 * @see AbstractCommand
 */
public class CommandHandler {
    private static final Map<Class<? extends AbstractCommand>, AbstractCommand> CLASS_MAP = new HashMap<>();
    private static final Map<String, AbstractCommand> REACTION_COMMAND_MAP = new HashMap<>();
    private static final Map<String, AbstractCommand> EXACT_COMMAND_MAP = new HashMap<>();
    private static final Map<String, Object> COMMANDS_MAP = new HashMap<>();
    private static final String UNKNOWN_COMMAND_EMOTICON = "grey_question", EXCEPTION_FOR_METHOD = "exclamation";
    private static final List<String> OPEN_EDIT_MESSAGES = new MemoryManagementService.ManagedList<>(30000);
    static {
        Map<Class<? extends AbstractCommand>, Set<AbstractCommand>> typeMap = new HashMap<>();
        new Reflections(Reference.BASE_PACKAGE).getSubTypesOf(AbstractCommand.class).forEach(clazz -> {
            try {
                AbstractCommand command = clazz.newInstance();
                CLASS_MAP.put(clazz, command);
                typeMap.computeIfAbsent(command.getSuperCommandType(), s -> new HashSet<>()).add(command);
            } catch (InstantiationException e) {
                Log.log("Exception attempting to initialize command: " + clazz.getName(), e);
            } catch (IllegalAccessException e) {
                Log.log("Malformed root command: " + clazz.getName(), e);
            } catch (DevelopmentException e) {
                Log.log("DevelopmentException while initializing command: " + clazz.getName(), e);
            } catch (RuntimeException e){
                Log.log("Exception while initializing command: " + clazz.getName(), e);
            }
        });
        typeMap.get(null).forEach(command -> load(command, typeMap));
        // alias loading
        typeMap.values().forEach(commands -> commands.forEach(command -> command.getEmoticonAliases().forEach(s -> REACTION_COMMAND_MAP.put(EmoticonHelper.getChars(s), command))));
        CLASS_MAP.values().forEach(command -> command.getNames().forEach(s -> {
            String[] strings = s.split(" ");
            Map map = COMMANDS_MAP;
            for (String string : strings) {
                map = (Map<String, Object>) map.computeIfAbsent(string, st -> new HashMap(2));
            }
            map.put("", command);
            EXACT_COMMAND_MAP.put(s, command);
        }));
        EventDistributor.register(CommandHandler.class);
    }

    private static void load(AbstractCommand superCommand, Map<Class<? extends AbstractCommand>, Set<AbstractCommand>> typeMap){
        superCommand.load();
        if (typeMap.containsKey(superCommand.getClass())) typeMap.get(superCommand.getClass()).forEach(command -> load(command, typeMap));
    }

    /**
     * Forces the initialization of this class
     */
    public static void initialize(){
        Log.log("Command Handler initialized");
    }

    /**
     * Gets the command instance by the class type
     *
     * @param commandType the command type
     * @return the command instance
     */
    public static AbstractCommand getCommand(Class<? extends AbstractCommand> commandType){
        return CLASS_MAP.get(commandType);
    }

    /**
     * The method to get a command from a reaction name
     *
     * @param reactionName the reaction name
     * @return the abstract command whose ailiase is the reaction chars
     */
    public static AbstractCommand getReactionCommand(String reactionName){
        return REACTION_COMMAND_MAP.get(EmoticonHelper.getChars(reactionName));
    }

    /**
     * Gets the command for the name given expecting no arguments
     *
     * @param name the exact name of the command
     * @return the exact command
     */
    public static AbstractCommand getCommand(String name){
        return EXACT_COMMAND_MAP.get(name);
    }

    /**
     * Gets the command and its arguments from a sting.
     *
     * @param in the string to derive the command
     *           and command parameters from
     * @return the command and parameters for that command
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
            in = in.substring(strings[i].length());
            while (true){
                if (!in.startsWith(" ")){
                    break;
                }
                in = in.substring(1);
            }
        }
        return new Pair<>(command, in);
    }

    /**
     * The method to attempt to run a command.
     *
     * @param string the command user args or reacted message content
     * @param user the invoker
     * @param message the message reacted to or sent to invoke the command
     * @param reaction the reaction that invoked this command, if applicable
     */
    private static boolean attemptInvocation(String string, User user, Message message, Reaction reaction){
        if (string == null){// can happen
            return false;
        }
        AbstractCommand command;
        if (message.getGuild() == null){
            boolean prefixTrimmingExempt = false;
            for (int i = 0; i < string.length(); i++) {
                if (Character.isLetterOrDigit(string.indexOf(i))){
                    prefixTrimmingExempt = true;
                    break;
                }
            }
            if (!prefixTrimmingExempt){
                while (!Character.isLetterOrDigit(string.charAt(0))){
                    string = string.substring(1);
                }
            }
        }else{
            String pref = ConfigHandler.getSetting(GuildPrefixConfig.class, message.getGuild());
            if (string.startsWith(pref)){
                string = FormatHelper.trimFront(string.substring(pref.length()));
            }else{
                if ((command = REACTION_COMMAND_MAP.get(string)) != null){
                    try{if (command.hasPermission(user, message.getGuild()) && command.checkCoolDown(message.getChannel(), user) && command.interpretSuccess(command.invoke(user, message.getShard(), message.getChannel(), message.getGuild(), message, reaction, string))){
                        command.invoked(message.getChannel(), user);
                        return true;
                    }
                    }catch(Exception ignored){}
                }
                return false;
            }
        }
        Pair<AbstractCommand, String> pair = reaction == null ? getMessageCommand(string) : ((command = getReactionCommand(reaction.getChars())) == null ? null : new Pair<>(command, null));
        if (pair != null){
            command = pair.getKey();
            Reaction r = message.getReactionByName(UNKNOWN_COMMAND_EMOTICON);
            if (r != null){
                message.removeReaction(r);
            }
            if (!command.hasPermission(user, message.getGuild())){
                if (reaction == null){
                    new MessageMaker(user, message).append("You do not have permission to use that command.").send();
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
                    command.invoked(message.getChannel(), user);
                }
                if (message.getReactionByName(EXCEPTION_FOR_METHOD) != null){
                    message.removeReactionByName(EXCEPTION_FOR_METHOD);
                }
                return invoked;
            } catch (BotException e){
                new MessageMaker(user, message).asExceptionMessage(e).send();
                message.addReactionByName(EXCEPTION_FOR_METHOD);
                return false;
            }
        }else{
            message.addReactionByName(UNKNOWN_COMMAND_EMOTICON);
            OPEN_EDIT_MESSAGES.add(message.getID());
            return false;
        }
    }

    /**
     * The monitoring for arg based commands
     *
     * @param event the monitored event
     */
    @EventListener
    public static void handle(DiscordMessageReceivedEvent event){
        attemptInvocation(event.getMessage().getContent(), event.getMessage().getAuthor(), event.getMessage(), null);
    }

    /**
     * The monitoring for reaction aliased commands
     *
     * @param event the monitored event
     */
    @EventListener
    public static void handle(DiscordReactionEvent event){
        if (!event.getUser().equals(DiscordClient.getOurUser())){
            attemptInvocation(event.getMessage().getContent(), event.getUser(), event.getMessage(), event.getReaction());
        }
    }

    /**
     * Monitoring for a reattempt at a command through editing
     *
     * @param event the monitored event
     */
    @EventListener
    public static void handle(DiscordMessageEditEvent event){
        if (!event.getMessage().getAuthor().equals(DiscordClient.getOurUser()) && OPEN_EDIT_MESSAGES.contains(event.getMessage().getID())){
            if (attemptInvocation(event.getMessage().getContent(), event.getMessage().getAuthor(), event.getMessage(), null)){
                OPEN_EDIT_MESSAGES.remove(event.getMessage().getID());
            }
        }
    }
}
