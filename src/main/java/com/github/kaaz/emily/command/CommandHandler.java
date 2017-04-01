package com.github.kaaz.emily.command;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.guild.GuildPrefixConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageHelper;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceivedEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.kaaz.emily.exeption.BotException;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Log;
import javafx.util.Pair;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
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
    private static final Map<String, Object> COMMANDS_MAP = new HashMap<>();
    static {
        Map<Class<? extends AbstractCommand>, Set<Class<? extends AbstractCommand>>> typeMap = new HashMap<>();
        new Reflections("com.github.kaaz.emily.command.commands").getSubTypesOf(AbstractCommand.class).forEach(clazz -> {
            Class<?>[] cTypes = clazz.getConstructors()[0].getParameterTypes();
            if (cTypes.length == 0){
                typeMap.computeIfAbsent(null, c -> new HashSet<>()).add(clazz);
            }else{
                typeMap.computeIfAbsent((Class<? extends AbstractCommand>) cTypes[0], c -> new HashSet<>()).add(clazz);
            }
        });
        load(null, typeMap.get(null), typeMap);
        CLASS_MAP.values().forEach(command -> command.getNames().forEach(s -> {
            String[] strings = s.split(" ");
            Map map = COMMANDS_MAP;
            for (String string : strings) {
                map = (Map<String, Object>) map.computeIfAbsent(string, st -> new HashMap(2));
            }
            map.put("", command);
        }));
        EventDistributor.register(CommandHandler.class);
    }

    private static <S extends AbstractCommand> void load(S superCommand, Set<Class<? extends AbstractCommand>> subCommands, Map<Class<? extends AbstractCommand>, Set<Class<? extends AbstractCommand>>> typeMap){
        if (subCommands == null){
            return;
        }
        subCommands.forEach(clazz -> {
            try {
                AbstractCommand command = superCommand == null ? clazz.newInstance() : (AbstractCommand) clazz.getConstructors()[0].newInstance(superCommand);
                CLASS_MAP.put(clazz, command);
                command.getEmoticonAliases().forEach(s -> REACTION_COMMAND_MAP.put(EmoticonHelper.getChars(s), command));
                load(command, typeMap.get(command.getClass()), typeMap);
            } catch (InstantiationException e) {
                Log.log("Exception attempting to initialize command: " + clazz.getName(), e);
            } catch (IllegalAccessException e) {
                Log.log("Malformed root command: " + clazz.getName(), e);
            } catch (InvocationTargetException e) {
                Log.log("Exception while initializing command: " + clazz.getName(), e);
            }
        });
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
        return REACTION_COMMAND_MAP.get(reactionName);
    }

    /**
     * Gets the command and its arguments from a sting.
     *
     * @param in the string to derive the command
     *           and command parameters from
     * @return the command and parameters for that command
     */
    public static Pair<AbstractCommand, String> getMessageCommand(String in){
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
    public static void attemptInvocation(String string, User user, Message message, Reaction reaction){
        if (string == null){// can happen
            return;
        }
        if (message.getGuild() != null){
            String pref = ConfigHandler.getSetting(GuildPrefixConfig.class, message.getGuild());
            if (string.startsWith(pref)){
                string = string.substring(pref.length());
                while (true){
                    if (!string.startsWith(" ")){
                        break;
                    }
                    string = string.substring(1);
                }
            }else{
                return;
            }
        }
        AbstractCommand command;
        Pair<AbstractCommand, String> pair = reaction == null ? getMessageCommand(string) : ((command = getReactionCommand(reaction.getChars())) == null ? null : new Pair<>(command, null));
        if (pair != null){
            if (!pair.getKey().hasPermission(user, message.getGuild())){
                if (reaction == null){
                    new MessageHelper(user, message.getChannel()).appendTranslation("You do not have permission to use that command.").send();
                }
                return;
            }
            if (!pair.getKey().checkCoolDown(message.getGuild(), message.getChannel(), user)){
                if (reaction == null){
                    new MessageHelper(user, message.getChannel()).appendTranslation("You can not use that command so soon.").send();
                }
                return;
            }
            try {
                if (pair.getKey().invoke(user, message, reaction, pair.getValue())){
                    pair.getKey().invoked(message.getGuild(), message.getChannel(), user);
                }
            } catch (BotException e){
                new MessageHelper(user, message.getChannel()).asExceptionMessage(e).send();
            }
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
        attemptInvocation(event.getMessage().getContent(), event.getUser(), event.getMessage(), event.getReaction());
    }
}
