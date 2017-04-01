package com.github.kaaz.emily.command;

import com.github.kaaz.emily.command.anotations.Context;
import com.github.kaaz.emily.command.anotations.Convert;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Playlist;
import com.github.kaaz.emily.config.configs.guild.UserNamesConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageHelper;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Log;
import javafx.util.Pair;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Made by nija123098 on 3/27/2017.
 */
public class InvocationObjectGetter {
    private static final Map<Class<?>, Map<String, InvocationGetter<?>>> CONTEXT_MAP = new HashMap<>();
    private static final Map<Class<?>, ArgumentConverter<?>> CONVERTER_MAP = new HashMap<>();

    static {
        addContext(User.class, "invoker", (user, message, reaction, args) -> user);
        addContext(Message.class, "invoker", (user, message, reaction, args) -> message);
        addContext(Reaction.class, "invoker", (user, message, reaction, args) -> reaction);
        addContext(Guild.class, "location", (user, message, reaction, args) -> message.getGuild());
        addContext(Channel.class, "location", (user, message, reaction, args) -> message.getChannel());
        addContext(Presence.class, "invoker", (user, message, reaction, args) -> user.getPresence());
        addContext(String.class, "args", (user, message, reaction, args) -> args);
        addContext(String[].class, "args", (user, message, reaction, args) -> FormatHelper.reduceRepeats(args, ' ').split(" "));
        addContext(MessageHelper.class, "", (user, message, reaction, args) -> new MessageHelper(user, message.getChannel()));
        addContext(VoiceChannel.class, "location", (user, message, reaction, args) -> message.getGuild().getConnectedVoiceChannel());
        addContext(Shard.class, "location", (user, message, reaction, args) -> message.getShard());
        addContext(Region.class, "location", (user, message, reaction, args) -> message.getGuild().getRegion());
        addContext(Attachment[].class, "invoker", (user, message, reaction, args) -> {
            List<Attachment> attachments = message.getAttachments();
            return attachments.toArray(new Attachment[attachments.size()]);
        });
    }

    private static <T> void addContext(Class<T> clazz, String label, InvocationGetter<T> invocationGetter){
        CONTEXT_MAP.computeIfAbsent(clazz, c -> {
            Map<String, InvocationGetter<?>> map = new HashMap<>(1);
            map.put("", invocationGetter);
            return map;
        });
        CONTEXT_MAP.get(clazz).put(label, invocationGetter);
    }

    @FunctionalInterface
    private interface InvocationGetter<E>{
        E getObject(User user, Message message, Reaction reaction, String args);
    }

    static {
        addConverter(Channel.class, (user, message, reaction, args) -> {
            boolean isMention = args.startsWith("<#");
            String arg = args.split(" ")[0].replace("<#", "").replace(">", "");
            int length = (isMention ? 3 : 0) + arg.length();
            Channel channel = DiscordClient.getChannelByID(arg);
            if (channel != null){
                return new Pair<>(channel, length);
            }
            if (message.getGuild() == null){
                return new Pair<>(message.getChannel(), length);
            }
            List<Channel> channels = message.getGuild().getChannelsByName(arg);
            if (channels.size() == 1){
                return new Pair<>(channels.get(0), length);
            }
            throw new ArgumentException("No channel identified by that name");
        });
        addConverter(User.class, (user, message, reaction, args) -> {
            String arg = args.split(" ")[0].replace("<@", "").replace("!", "").replace(">", "");
            User u = DiscordClient.getUserByID(arg);
            if (u != null){
                return new Pair<>(user, args.split(" ")[0].length());
            }
            if (message.getGuild() == null){
                throw new ArgumentException("Commands with user names can not be used in private channels");
            }
            List<User> users = new ArrayList<>();
            Guild guild = message.getGuild();
            ConfigHandler.getSetting(UserNamesConfig.class, message.getGuild()).forEach(s -> {
                if (args.startsWith(s) && (args.length() == s.length() || args.charAt(s.length()) == ' ')){
                    users.addAll(guild.getUsersByName(s));
                    if (users.size() > 1){
                        throw new ArgumentException("To many users by that name");
                    }
                }
            });
            if (users.size() == 0){
                throw new ArgumentException("No users by that name");
            }
            return new Pair<>(users.iterator().next(), users.get(0).getName().length());
        });
        addConverter(Playlist.class, (user, message, reaction, args) -> {
            String arg = args.split(" ")[0];
            Playlist playlist = Playlist.getPlaylist(user, message.getGuild(), arg);
            if (playlist == null){
                throw new ArgumentException("No playlist identified with that name");
            }
            return new Pair<>(playlist, arg.length());
        });
        addConverter(Guild.class, (user, message, reaction, args) -> {
            Guild guild = Guild.getGuild(args.split(" ")[0]);
            if (guild != null){
                return new Pair<>(guild, guild.getID().length());
            }
            for (Guild g : DiscordClient.getGuilds()){
                String name = g.getName();
                if (args.startsWith(name) && (args.length() == name.length() || args.charAt(name.length()) == ' ')){
                    if (guild != null){
                        throw new ArgumentException("To many guilds are named that");
                    }
                    guild = g;
                }
            }
            if (guild == null){
                throw new ArgumentException("No guilds named that");
            }
            return new Pair<>(guild, guild.getName().length());
        });
        addConverter(Shard.class, (user, message, reaction, args) -> {
            String arg = args.split(" ")[0];
            try {
                return new Pair<>(Shard.getShard(Integer.parseInt(arg)), arg.length());
            } catch (Exception e){
                throw new ArgumentException("Not a valid shard ID");
            }
        });
        addConverter(Message.class, (user, message, reaction, args) -> {
            if (args.startsWith("this")){
                return new Pair<>(message, 4);
            }
            String arg = args.split(" ")[0];
            Message m = Message.getMessage(arg);
            if (m == null){
                throw new ArgumentException("No message with that ID");
            }
            return new Pair<>(m, arg.length());
        });
        addConverter(Region.class, (user, message, reaction, args) -> {
            String arg = args.split(" ")[0];
            Region region = Region.getRegion(arg);
            if (region != null){
                return new Pair<>(region, arg.length());
            }
            for (Region r : DiscordClient.getRegions()){
                if (args.startsWith(r.getName())){
                    return new Pair<>(r, r.getName().length());
                }// Discord isn't trying to trick us
            }
            throw new ArgumentException("Not a valid shard ID");
        });
        addConverter(Role.class, (user, message, reaction, args) -> {
            if (message.getGuild() == null){
                throw new ArgumentException("No roles are in a DM");
            }
            String arg = args.split(" ")[0];
            Role role = message.getGuild().getRoleByID(arg);
            if (role != null){
                return new Pair<>(role, role.getID().length());
            }
            for (Role r : message.getGuild().getRoles()){
                if (args.startsWith(r.getName()) && (args.length() == arg.length() || args.charAt(arg.length()) == ' ')){
                    if (role != null){
                        throw new ArgumentException("To many roles named that");
                    }
                    role = r;
                }
            }
            if (role == null){
                throw new ArgumentException("There are no roles by that name");
            }
            return new Pair<>(role, role.getName().length());
        });
    }

    private static <T> void addConverter(Class<T> clazz, ArgumentConverter<T> argumentConverter){
        CONVERTER_MAP.put(clazz, argumentConverter);
    }

    @FunctionalInterface
    private interface ArgumentConverter<E>{
        Pair<E, Integer> getObject(User user, Message message, Reaction reaction, String args);
    }

    /**
     * Forces the initialization of this class
     */
    public static void initialize(){
        Log.log("Invocation Object Getter initialized");
    }

    static Object[] replace(Parameter[] parameters, Object[] objects, User user, Message message, Reaction reaction, String args){
        int commandArgIndex = 0;
        for (int i = 0; i < parameters.length; i++) {
            try {
                if (parameters[i].isAnnotationPresent(Context.class) || parameters[i].getAnnotations().length == 0){// null might be an instance of Context
                    objects[i] = CONTEXT_MAP.get(parameters[i].getType()).get(parameters[i].getAnnotations().length == 0 ? "" : parameters[i].getAnnotation(Context.class).value()).getObject(user, message, reaction, args);
                }else if (parameters[i].isAnnotationPresent(Convert.class)){
                    ++commandArgIndex;
                    Pair<Object, Integer> pair = (Pair<Object, Integer>) CONVERTER_MAP.get(parameters[i].getType()).getObject(user, message, reaction, args);
                    objects[i] = pair.getKey();
                    args = args.substring(pair.getValue());
                    while (true){
                        if (args.length() == 0 || args.charAt(0) != ' '){
                            break;
                        }
                        args = args.substring(1);
                    }
                }
            } catch (ArgumentException e){
                if (parameters[i].isAnnotationPresent(Convert.class) && parameters[i].getAnnotation(Convert.class).optional()){
                    continue;
                }
                e.setParameter(commandArgIndex);
                e.setArgs(parameters);
                throw e;
            }
        }
        return objects;
    }
}
