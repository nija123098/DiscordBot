package com.github.kaaz.emily.command;

import com.github.kaaz.emily.audio.Playlist;
import com.github.kaaz.emily.command.anotations.Context;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.config.*;
import com.github.kaaz.emily.config.configs.guild.GuildActivePlaylistConfig;
import com.github.kaaz.emily.config.configs.guild.UserNamesConfig;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.ContextException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.fun.slot.SlotPack;
import com.github.kaaz.emily.information.subsription.SubscriptionLevel;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.*;
import javafx.util.Pair;

import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 3/27/2017.
 */
public class InvocationObjectGetter {
    private static final Map<Class<?>, Map<ContextType, Pair<InvocationGetter<?>, Set<ContextRequirement>>>> CONTEXT_MAP = new HashMap<>();
    private static final Map<Class<?>, Pair<ArgumentConverter<?>, Set<ContextRequirement>>> CONVERTER_MAP = new HashMap<>();

    static {
        addContext(User.class, ContextType.INVOKER, (user, shard, channel, guild, message, reaction, args) -> user, ContextRequirement.USER);
        addContext(Message.class, ContextType.INVOKER, (user, shard, channel, guild, message, reaction, args) -> message, ContextRequirement.MESSAGE);
        addContext(Reaction.class, ContextType.INVOKER, (user, shard, channel, guild, message, reaction, args) -> reaction, ContextRequirement.REACTION);
        addContext(Guild.class, ContextType.LOCATION, (user, shard, channel, guild, message, reaction, args) -> guild, ContextRequirement.GUILD);
        addContext(GuildUser.class, ContextType.INVOKER, (user, shard, channel, guild, message, reaction, args) -> GuildUser.getGuildUser(guild, user), ContextRequirement.GUILD, ContextRequirement.USER);
        addContext(Channel.class, ContextType.LOCATION, (user, shard, channel, guild, message, reaction, args) -> channel, ContextRequirement.CHANNEL);
        addContext(Presence.class, ContextType.INVOKER, (user, shard, channel, guild, message, reaction, args) -> user.getPresence(), ContextRequirement.USER);
        addContext(String.class, ContextType.ARGS, (user, shard, channel, guild, message, reaction, args) -> args, ContextRequirement.STRING);
        addContext(String[].class, ContextType.ARGS, (user, shard, channel, guild, message, reaction, args) -> FormatHelper.reduceRepeats(args, ' ').split(" "), ContextRequirement.STRING);
        addContext(MessageMaker.class, ContextType.DEFAULT, (user, shard, channel, guild, message, reaction, args) -> message == null ? new MessageMaker(user, channel) : new MessageMaker(user, message), ContextRequirement.CHANNEL);
        addContext(VoiceChannel.class, ContextType.LOCATION, (user, shard, channel, guild, message, reaction, args) -> {
            VoiceChannel target = user.getConnectedVoiceChannel(guild);
            if (target == null) throw new ContextException("You must be in a voice channel to use that command");
            return target;
        }, ContextRequirement.GUILD, ContextRequirement.USER);
        addContext(Shard.class, ContextType.LOCATION, (user, shard, channel, guild, message, reaction, args) -> shard, ContextRequirement.SHARD);
        addContext(Region.class, ContextType.LOCATION, (user, shard, channel, guild, message, reaction, args) -> guild.getRegion(), ContextRequirement.GUILD);
        addContext(Attachment[].class, ContextType.INVOKER, (user, shard, channel, guild, message, reaction, args) -> {
            List<Attachment> attachments = message.getAttachments();
            return attachments.toArray(new Attachment[attachments.size()]);
        }, ContextRequirement.MESSAGE);
        addContext(Playlist.class, ContextType.STATUS, (user, shard, channel, guild, message, reaction, args) -> ConfigHandler.getSetting(GuildActivePlaylistConfig.class, guild), ContextRequirement.GUILD);
        addContext(GuildAudioManager.class, ContextType.STATUS, (invoker, shard, channel, guild, message, reaction, args) -> {
            GuildAudioManager manager = GuildAudioManager.getManager(invoker.getConnectedVoiceChannel(guild), false);
            if (manager == null) throw new ContextException("You must be in a voice channel with Emily to use this command");
            return manager;
        });
        addContext(GuildAudioManager.class, ContextType.LOCATION, (invoker, shard, channel, guild, message, reaction, args) -> GuildAudioManager.getManager(invoker.getConnectedVoiceChannel(guild)));
        addContext(Track.class, ContextType.STATUS, (user, shard, channel, guild, message, reaction, args) -> {
            GuildAudioManager manager = GuildAudioManager.getManager(user.getConnectedVoiceChannel(guild), false);
            if (manager == null || manager.currentTrack() == null) throw new ContextException("No track is currently playing");
            return manager.currentTrack();
        }, ContextRequirement.GUILD, ContextRequirement.USER);
        addContext(ContextPack.class, ContextType.DEFAULT, ContextPack::new);
        addContext(Float.class, ContextType.NONE, (invoker, shard, channel, guild, message, reaction, args) -> null);
        addContext(Configurable.class, ContextType.DEFAULT, (user, shard, channel, guild, message, reaction, args) -> null);
    }// ^ is for the optional on configurable conversions

    private static <T> void addContext(Class<T> clazz, ContextType contextType, InvocationGetter<T> invocationGetter, ContextRequirement...requirements){
        Pair<InvocationGetter<?>, Set<ContextRequirement>> pair = new Pair<>(invocationGetter, EnumHelper.getSet(ContextRequirement.class, requirements));
        CONTEXT_MAP.computeIfAbsent(clazz, c -> {
            Map<ContextType, Pair<InvocationGetter<?>, Set<ContextRequirement>>> map = new HashMap<>(1);
            map.put(ContextType.DEFAULT, pair);
            return map;
        });
        CONTEXT_MAP.get(clazz).put(contextType, pair);
    }

    public static Set<ContextRequirement> getContextRequirements(Class<?> type, ContextType contextType){
        return CONTEXT_MAP.get(type).get(contextType).getValue();
    }

    @FunctionalInterface
    private interface InvocationGetter<E>{
        E getObject(User invoker, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args);
    }

    static {
        addConverter(Channel.class, (user, shard, channel, guild, message, reaction, args) -> {
            boolean isMention = args.startsWith("<#");
            String arg = args.split(" ")[0].replace("<#", "").replace(">", "");
            int length = (isMention ? 3 : 0) + arg.length();
            Channel target = DiscordClient.getChannelByID(arg);
            if (target != null){
                return new Pair<>(target, length);
            }
            ContextException.checkGuild(guild);
            List<Channel> channels = guild.getChannelsByName(arg);
            if (channels.size() == 1){
                return new Pair<>(channels.get(0), length);
            }
            throw new ArgumentException("No channel identified by that name, try using an ID or mention");
        });
        addConverter(User.class, (user, shard, channel, guild, message, reaction, args) -> {
            User u = DiscordClient.getUserByID(args.split(" ")[0].replace("<@", "").replace("!", "").replace(">", ""));
            if (u != null) return new Pair<>(u, args.split(" ")[0].length());
            if (guild == null) throw new ArgumentException("Commands with user names can not be used in private channels");
            List<User> users = new ArrayList<>(3);
            ConfigHandler.getSetting(UserNamesConfig.class, message.getGuild()).forEach(s -> {
                if (args.startsWith(s) && ((args.length() == s.length() || args.charAt(s.length()) == ' '))){
                    users.addAll(guild.getUsersByName(s));
                    if (users.size() > 1){
                        throw new ArgumentException("To many users by that name");
                    }
                }
            });
            if (users.size() == 0) throw new ArgumentException("No users by that name, try an ID or mention");
            return new Pair<>(users.iterator().next(), users.get(0).getName().length());
        });
        addConverter(Playlist.class, (user, shard, channel, guild, message, reaction, args) -> {
            if (args.toLowerCase().startsWith("global")) return new Pair<>(Playlist.GLOBAL_PLAYLIST, args.equalsIgnoreCase("global playlist") ? 15 : 6);
            Playlist playlist = Playlist.getPlaylist(user, message.getGuild(), args);
            if (playlist == null) throw new ArgumentException("No playlist identified with that name");
            String[] strings = args.split(" ");
            return new Pair<>(playlist, strings[0].length() + strings[1].length() + 1);
        });
        addConverter(Guild.class, (user, shard, channel, guild, message, reaction, args) -> {
            Guild target = Guild.getGuild(args.split(" ")[0]);
            if (target != null) return new Pair<>(target, target.getID().length());
            for (Guild g : DiscordClient.getGuilds()){
                String name = g.getName();
                if (args.startsWith(name) && (args.length() == name.length() || args.charAt(name.length()) == ' ')){
                    if (target != null){
                        throw new ArgumentException("To many guilds are named that");
                    }
                    target = g;
                }
            }
            if (target == null) throw new ArgumentException("No guilds named that, try an ID");
            return new Pair<>(target, target.getName().length());
        });
        addConverter(Shard.class, (user, shard, channel, guild, message, reaction, args) -> {
            String arg = args.split(" ")[0];
            if (arg.toLowerCase().equals("this")){
                return new Pair<>(shard, 4);
            }
            try {
                int i = Integer.parseInt(arg);
                Shard s = Shard.getShard(i);
                if (s == null) throw new ArgumentException("That shard does not exist, try a " + (i < 0 ? "higher" : "lower") + " number");
                return new Pair<>(s, arg.length());
            } catch (Exception e){
                throw new ArgumentException("Not a valid shard ID");
            }
        });
        addConverter(Message.class, (user, shard, channel, guild, message, reaction, args) -> {
            if (args.startsWith("this")) return new Pair<>(message, 4);
            String arg = args.split(" ")[0];
            Message m = Message.getMessage(arg);
            if (m == null) throw new ArgumentException("No message with that ID");
            return new Pair<>(m, arg.length());
        });
        addConverter(Region.class, (user, shard, channel, guild, message, reaction, args) -> {
            if (guild != null && args.toLowerCase().startsWith("this")) return new Pair<>(guild.getRegion(), 4);
            String arg = args.split(" ")[0];
            Region region = Region.getRegion(arg);
            if (region != null) return new Pair<>(region, arg.length());
            for (Region r : DiscordClient.getRegions()){
                if (args.startsWith(r.getName())){
                    return new Pair<>(r, r.getName().length());
                }// Discord isn't trying to trick us
            }
            throw new ArgumentException("Not a valid region id ID");
        });
        addConverter(Role.class, (user, shard, channel, guild, message, reaction, args) -> {
            String arg = args.split(" ")[0];
            Role role = message.getGuild().getRoleByID(arg);
            if (role != null) return new Pair<>(role, role.getID().length());
            for (Role r : message.getGuild().getRoles()){
                if (args.startsWith(r.getName()) && (args.length() == arg.length() || args.charAt(arg.length()) == ' ')){
                    if (role != null){
                        throw new ArgumentException("To many roles named that");
                    }
                    role = r;
                }
            }
            if (role == null) throw new ArgumentException("There are no roles by that name");
            return new Pair<>(role, role.getName().length());
        });
        addConverter(ConfigLevel.class, (user, shard, channel, guild, message, reaction, args) -> {
            String arg = args.split(" ")[0].toUpperCase();// replace with ConfigLevel.getLevel
            try{return new Pair<>(ConfigLevel.valueOf(arg), arg.length());
            }catch(Exception ignored){}
            arg = arg.toUpperCase();
            if (arg.equals("GUILDUSER")) return new Pair<>(ConfigLevel.GUILD_USER, 9);
            if (arg.equals("GUILD USER")) return new Pair<>(ConfigLevel.GUILD_USER, 10);
            throw new ArgumentException("No such Configurable type");
        });
        addConverter(Boolean.class, (user, shard, channel, guild, message, reaction, args) -> {
            String arg = args.split(" ")[0];
            return new Pair<>(LanguageHelper.getBoolean(arg), arg.length());
        });
        addConverter(Integer.class, (user, shard, channel, guild, message, reaction, args) -> {
            String arg = args.split(" ")[0];
            return new Pair<>(LanguageHelper.getInteger(arg), arg.length());
        });
        addConverter(Float.class, (user, shard, channel, guild, message, reaction, args) -> {
            try {
                String arg = args.split(" ")[0];
                Float result = Float.parseFloat(arg);
                return new Pair<>(result, arg.length());
            } catch (Exception e){
                throw new ArgumentException("That is not a decimal number");
            }
        });
        addConverter(AbstractConfig.class, (user, shard, channel, guild, message, reaction, args) -> {
            AbstractConfig<?, ? extends Configurable> a = ConfigHandler.getConfig(args.split(" ")[0]);
            if (a == null) throw new ArgumentException("No such config");
            return new Pair<>(a, a.getName().length());
        });
        addConverter(Configurable.class, (user, shard, channel, guild, message, reaction, args) -> {
            AtomicReference<Pair<Configurable, Integer>> pair = new AtomicReference<>();
            CONVERTER_MAP.forEach((type, converter) -> {
                if (!Configurable.class.isAssignableFrom(type) || Configurable.class.equals(type) || pair.get() != null){
                    return;
                }
                try {
                    pair.set((Pair<Configurable, Integer>) converter.getKey().getObject(user, shard, channel, guild, message, reaction, args));
                } catch (Exception ignored){}
            });
            if (pair.get() == null) throw new ArgumentException("No configurable instance found");
            return pair.get();
        });
        addConverter(String.class, (invoker, shard, channel, guild, message, reaction, args) -> new Pair<>(args, args.length()));
        addConverter(BotRole.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            args = args.toUpperCase();
            BotRole role = null;
            try {
                role = BotRole.valueOf(args.split(" ")[0]);
            } catch (Exception ignored){}
            if (role == null) {
                for (BotRole r : BotRole.values()){
                    if (r.name().contains("_") && args.startsWith(r.name().replace("_", " "))){
                        role = r;
                        break;
                    }
                }
            }
            if (role != null) return new Pair<>(role, role.name().length());
            throw new ArgumentException("No BotRole found");
        });
        addConverter(AbstractCommand.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            Pair<AbstractCommand, String> command = CommandHandler.getMessageCommand(args);
            if (command == null) throw new ArgumentException("No such command found");
            return new Pair<>(command.getKey(), args.length() - command.getValue().length());
        });
        addConverter(Time.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            String first = args.split(" ")[0];
            return new Pair<>(new Time(first), first.length());
        });
        addConverter(SlotPack.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            try{return new FunctionPair<>(SlotPack.valueOf(args.split(" ")[0].toUpperCase()), pack -> pack.name().length());
            } catch (Exception e){
                throw new ArgumentException("Unrecognized pack name");
            }
        });
        addConverter(ModuleLevel.class, (invoker, shard, channel, guild, message, reaction, args) -> EnumHelper.getValue(ModuleLevel.class, args));
        addConverter(CommandGroup.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            try {
                Pair<ModuleLevel, Integer> pair = (Pair<ModuleLevel, Integer>) CONVERTER_MAP.get(ModuleLevel.class).getKey().getObject(invoker, shard, channel, guild, message, reaction, args);
                return new Pair<>(new CommandGroup(pair.getKey()), pair.getValue());
            } catch (ArgumentException ignored){}
            try {
                Pair<AbstractCommand, Integer> pair = (Pair<AbstractCommand, Integer>) CONVERTER_MAP.get(AbstractCommand.class).getKey().getObject(invoker, shard, channel, guild, message, reaction, args);
                return new Pair<>(new CommandGroup(pair.getKey()), pair.getValue());
            } catch (ArgumentException ignored){}
            throw new ArgumentException("Please indicate a command or a module");
        });
        addConverter(SubscriptionLevel.class, (invoker, shard, channel, guild, message, reaction, args) -> EnumHelper.getValue(SubscriptionLevel.class, args));
    }

    private static <T> void addConverter(Class<T> clazz, ArgumentConverter<T> argumentConverter, ContextRequirement...requirements){
        EnumSet<ContextRequirement> req = EnumHelper.getSet(ContextRequirement.class, requirements);
        req.add(ContextRequirement.STRING);
        CONVERTER_MAP.put(clazz, new Pair<>(argumentConverter, req));
    }

    public static Set<ContextRequirement> getConvertRequirements(Class<?> type, ContextType contextType){
        return CONVERTER_MAP.get(type).getValue();
    }

    public static <T> Pair<T, Integer> convert(Class<T> clazz, User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args){
        return (Pair<T, Integer>) CONVERTER_MAP.get(clazz).getKey().getObject(user, shard, channel, guild, message, reaction, args);
    }

    @FunctionalInterface
    private interface ArgumentConverter<E>{
        Pair<E, Integer> getObject(User invoker, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args);
    }

    /**
     * Forces the initialization of this class
     */
    public static void initialize(){
        Log.log("Invocation Object Getter initialized");
    }

    static Object[] replace(Parameter[] parameters, Object[] objects, User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args, boolean[] argOverride){
        int commandArgIndex = 0;
        for (int i = 0; i < parameters.length; i++) {
            try {
                if (parameters[i].isAnnotationPresent(Context.class) || parameters[i].getAnnotations().length == 0){// null might be an instance of Context
                    checkContextType(parameters[i].getType());
                    try {
                        objects[i] = CONTEXT_MAP.get(parameters[i].getType()).get(parameters[i].getAnnotations().length == 0 ? ContextType.DEFAULT : parameters[i].getAnnotation(Context.class).value()).getKey().getObject(user, shard, channel, guild, message, reaction, args);
                    } catch (Exception e){
                        if (!(parameters[i].isAnnotationPresent(Context.class) && parameters[i].getAnnotation(Context.class).softFail())){
                            throw e;
                        }
                    }
                }else if (parameters[i].isAnnotationPresent(Argument.class)){
                    if (argOverride.length > i && argOverride[commandArgIndex++]){
                        continue;
                    }
                    checkConvertType(parameters[i].getType());
                    Pair<Object, Integer> pair = (Pair<Object, Integer>) CONVERTER_MAP.get(parameters[i].getType()).getKey().getObject(user, shard, channel, guild, message, reaction, args);
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
                if (parameters[i].isAnnotationPresent(Argument.class) && parameters[i].getAnnotation(Argument.class).optional()){
                    if (parameters[i].getAnnotation(Argument.class).replacement() != ContextType.NONE) checkContextType(parameters[i].getType());
                    objects[i] = parameters[i].getAnnotation(Argument.class).replacement() == ContextType.NONE ? null : CONTEXT_MAP.get(parameters[i].getType()).get(parameters[i].getAnnotation(Argument.class).replacement()).getKey().getObject(user, shard, channel, guild, message, reaction, args);
                    continue;
                }
                e.setParameter(commandArgIndex);
                e.setArgs(parameters);
                throw e;
            }
        }
        return objects;
    }

    public static void checkConvertType(Class<?> type){
        if (!CONVERTER_MAP.containsKey(type)){
            throw new DevelopmentException("Can not convert objects of type: " + type.getSimpleName());
        }
    }

    public static void checkContextType(Class<?> type){
        if (!CONTEXT_MAP.containsKey(type)){
            throw new DevelopmentException("Can not get context for objects of type: " + type.getSimpleName());
        }
    }

    public static Object getTypeOf(Object...objects){
        Map<Class<?>, Object> map = null;
        for (Object o : objects){
            if (map == null){
                 map = new HashMap<>();
                 continue;
            }
            if (o == null){
                continue;
            }
            map.put(o.getClass(), o);
        }
        return map.get(objects[0]);
    }
}
