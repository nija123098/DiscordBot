package com.github.kaaz.emily.command;

import com.github.kaaz.emily.discordobjects.helpers.MessageHelper;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceivedEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Log;
import javafx.util.Pair;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.reflections.Reflections;

import java.util.HashMap;
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
        Reflections reflections = new Reflections("com.github.kaaz.emily.command.commands");
        Set<AbstractSuperCommand> superCommands = new ConcurrentHashSet<>();
        Set<AbstractSubCommand> subCommands = new ConcurrentHashSet<>();
        reflections.getSubTypesOf(AbstractCommand.class).forEach(clazz -> {
            try {
                AbstractCommand command = clazz.newInstance();
                if (command instanceof AbstractSuperCommand){
                    superCommands.add((AbstractSuperCommand) command);
                }else{
                    subCommands.add((AbstractSubCommand) command);
                }
                command.getEmoticonAliases().forEach(s -> REACTION_COMMAND_MAP.put(s, command));
                CLASS_MAP.put(clazz, command);
            } catch (InstantiationException e) {
                Log.log("Error while initializing command: " + clazz.getSimpleName(), e);
            } catch (IllegalAccessException e) {
                Log.log("Command improperly formed: " + clazz.getSimpleName(), e);
            }
        });
        Map<Package, AbstractSuperCommand> packageNameMap = new HashMap<>();
        superCommands.forEach(command -> packageNameMap.put(command.getClass().getPackage(), command));
        subCommands.forEach(command -> command.setSuperCommand(packageNameMap.get(command.getClass().getPackage())));
        CLASS_MAP.forEach((clazz, command) -> command.getNames().forEach(s -> {// todo optimize memory as well
            String[] strings = s.split(" ");
            Map map = COMMANDS_MAP;
            for (int i = 0; i < strings.length; i++) {
                map = (Map<String, Object>) map.computeIfAbsent(strings[i], st -> new HashMap(2));
            }
            map.put("", command);
        }));
        EventDistributor.register(CommandHandler.class);
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
        AbstractCommand command = null;
        int index = -1;
        Map<String, Object> map = COMMANDS_MAP;
        for (int i = 0; i < strings.length; ++i) {
            AbstractCommand com = (AbstractCommand) map.get("");
            if (com != null){
                command = com;
                index = i;
            }
            map = (Map<String, Object>) map.get(strings[i]);
            if (map == null){
                break;
            }
        }
        if (index == -1){
            return null;
        }
        String builder = in;
        for (int i = 0; i < index; i++) {
            builder = builder.substring(builder.indexOf(' ') + strings[i].length());
        }
        return new Pair<>(command, cutCommand(builder.substring(builder.indexOf(' ') + 1)));
    }

    /**
     * The method to cut all command aliases from the command parameter
     *
     * @param string the full command aliases and parameters
     * @param strings the command aliases broken up by spaces
     * @return the command parameters
     */
    private static String cutCommand(String string, String...strings){
        String builder = string;
        for (String st : strings){
            builder = builder.substring(builder.indexOf(' ') + st.length());
        }
        return builder.substring(builder.indexOf(' ') + 1);
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
        AbstractCommand command;
        Pair<AbstractCommand, String> pair = reaction == null ? getMessageCommand(string) : ((command = getReactionCommand(reaction.getName())) == null ? null : new Pair<>(command, null));
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
            if (pair.getKey().invoke(user, message, reaction, pair.getValue())){
                pair.getKey().invoked(message.getGuild(), message.getChannel(), user);
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
