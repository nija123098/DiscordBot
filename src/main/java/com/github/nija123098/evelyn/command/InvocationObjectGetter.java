package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.audio.GlobalPlaylist;
import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.audio.configs.guild.GuildPlaylistsConfig;
import com.github.nija123098.evelyn.audio.configs.playlist.UserPlaylistsConfig;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.config.configs.guild.GuildActivePlaylistConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.ContextException;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.fun.gamestructure.AbstractGame;
import com.github.nija123098.evelyn.fun.gamestructure.GameHandler;
import com.github.nija123098.evelyn.fun.gamestructure.Team;
import com.github.nija123098.evelyn.information.currency.Cryptocurrency;
import com.github.nija123098.evelyn.util.*;
import javafx.util.Pair;

import java.awt.*;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The source for getting context and arguments for arguments for a command.
 *
 * The first part of this class deals with context and simply
 * gets instances of objects based on a command's context.
 * All functions are defined by {@link InvocationGetter}
 * and stored in CONTEXT_MAP for invocation.
 *
 * The second part of the class contains definitions for
 * converting parts of {@link String} instances to objects.
 * Functions are defined by {@link ArgumentConverter} which
 * returns a {@link Pair} with the object as the key and the
 * number of characters used to define the object as the value.
 * {@link Pair} relationships are not constant and should not
 * be confused to have a 1 to 1 behavior.
 *
 * The third part of the class is the utilization of the defined
 * context and conversion functions.  Primarily converting to get
 * an array of objects for command invocation.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class InvocationObjectGetter {
    private static final Map<Class<?>, Map<ContextType, Pair<InvocationGetter<?>, Set<ContextRequirement>>>> CONTEXT_MAP = new HashMap<>();
    private static final Map<Class<?>, Pair<ArgumentConverter<?>, Set<ContextRequirement>>> CONVERTER_MAP = new HashMap<>();

    static {
        EventDistributor.register(UserNameMonitor.class);
        addContext(User.class, ContextType.INVOKER, (user, shard, channel, guild, message, reaction, args) -> user, ContextRequirement.USER);
        addContext(Message.class, ContextType.INVOKER, (user, shard, channel, guild, message, reaction, args) -> message, ContextRequirement.MESSAGE);
        addContext(Reaction.class, ContextType.INVOKER, (user, shard, channel, guild, message, reaction, args) -> reaction, ContextRequirement.REACTION);
        addContext(Guild.class, ContextType.LOCATION, (user, shard, channel, guild, message, reaction, args) -> guild, ContextRequirement.GUILD);
        addContext(GuildUser.class, ContextType.INVOKER, (user, shard, channel, guild, message, reaction, args) -> GuildUser.getGuildUser(guild, user), ContextRequirement.GUILD, ContextRequirement.USER);
        addContext(Channel.class, ContextType.LOCATION, (user, shard, channel, guild, message, reaction, args) -> channel, ContextRequirement.CHANNEL);
        addContext(Presence.class, ContextType.INVOKER, (user, shard, channel, guild, message, reaction, args) -> user.getPresence(), ContextRequirement.USER);
        addContext(String.class, ContextType.ARGS, (user, shard, channel, guild, message, reaction, args) -> args, ContextRequirement.STRING);
        String[] EMPTY_STRING_ARRAY = new String[0];
        addContext(String[].class, ContextType.ARGS, (user, shard, channel, guild, message, reaction, args) -> args.isEmpty() ? EMPTY_STRING_ARRAY : FormatHelper.reduceRepeats(args, ' ').split(" "), ContextRequirement.STRING);
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
        addContext(GuildAudioManager.class, ContextType.LOCATION, (invoker, shard, channel, guild, message, reaction, args) -> {
            VoiceChannel voiceChannel = invoker.getConnectedVoiceChannel(guild);
            if (voiceChannel == null) throw new ContextException("You must be in a voice channel to use that command");
            return GuildAudioManager.getManager(voiceChannel, true);
        }, ContextRequirement.GUILD);
        addContext(Track.class, ContextType.STATUS, (user, shard, channel, guild, message, reaction, args) -> {
            GuildAudioManager manager = GuildAudioManager.getManager(user.getConnectedVoiceChannel(guild), false);
            if (manager == null || manager.currentTrack() == null) throw new ContextException("No track is currently playing");
            return manager.currentTrack();
        }, ContextRequirement.GUILD, ContextRequirement.USER);
        addContext(AbstractGame.class, ContextType.DEFAULT, (invoker, shard, channel, guild, message, reaction, args) -> GameHandler.getGame(guild, message.getAuthor()), ContextRequirement.GUILD, ContextRequirement.USER);
        addContext(ContextPack.class, ContextType.DEFAULT, ContextPack::new);
        addContext(Float.class, ContextType.NONE, (invoker, shard, channel, guild, message, reaction, args) -> null);
        addContext(Configurable.class, ContextType.DEFAULT, (user, shard, channel, guild, message, reaction, args) -> null);
    }// ^ is for the optional on configurable conversions

    /**
     * Adds a context class type specified by the class instance.
     *
     * @param clazz the context class type.
     * @param contextType the enum representation for use of when multiple contexts are gotten.
     * @param invocationGetter the function to define the context return.
     * @param requirements enum representations of context requirements for requiring certain contexts in command invocation.
     * @param <T> the type of objects to return.
     */
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

    /**
     * A function to get context instances for.
     *
     * @param <E> the type of object to return.
     */
    @FunctionalInterface
    private interface InvocationGetter<E>{
        E getObject(User invoker, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args);
    }

    static {
        addConverter(Channel.class, (user, shard, channel, guild, message, reaction, args) -> {
            boolean isMention = args.startsWith("<#");
            String arg = args.split(" ")[0].replace("<#", "").replace(">", "");
            int length = (isMention ? 3 : 0) + arg.length();
            Channel target = Channel.getChannel(arg);
            if (target != null){
                return new Pair<>(target, length);
            }
            if (guild == null) throw new ContextException("You need to be in a guild to use that command");
            List<Channel> channels = guild.getChannelsByName(arg);
            if (channels.size() == 1) return new Pair<>(channels.get(0), length);
            List<VoiceChannel> voiceChannels = guild.getVoiceChannelsByName(arg);
            if (voiceChannels.size() == 1) return new Pair<>(voiceChannels.get(0), length);
            throw new ArgumentException((voiceChannels.isEmpty() && channels.isEmpty() ? "No channel is " : "Too many channels are") + " identified by that name, try using an ID or mention");
        });
        addConverter(VoiceChannel.class, (invoker, shard, channel, guild, message, reaction, args) -> (Pair<VoiceChannel, Integer>) CONVERTER_MAP.get(Channel.class).getKey().getObject(invoker, shard, channel, guild, message, reaction, args));
        addConverter(User.class, (user, shard, channel, guild, message, reaction, args) -> {
            if (args.equalsIgnoreCase("me")) return new Pair<>(user, 2);
            String first = args.split(" ")[0];
            User u = User.getUser(first);
            if (u != null) return new Pair<>(u, first.length());
            if (guild == null) throw new ArgumentException("Commands with user names can not be used in private channels");
            List<User> users = guild.getUsersByName(first);
            if (users.size() == 1) return new Pair<>(users.get(0), first.length());
            String match = StringHelper.getGoodMatch(args, new ArrayList<>(UserNameMonitor.getNames(guild)));
            if (match == null) throw new ArgumentException("No users by that name, try an ID, mention, or good old copy and paste");
            else users.addAll(guild.getUsersByName(match));
            if (users.size() > 1) throw new ArgumentException("There are too many users named that!");
            int length = 0;
            if (match.contains(" ")){
                int matchSplitLength = match.split(" ").length;
                String[] argsSplit = args.split(" ");
                if (matchSplitLength >= argsSplit.length) length = args.length();
                else for (int i = 0; i < matchSplitLength; i++) {
                    length += argsSplit[i].length();
                }
            }else length = args.split(" ")[0].length();
            return new Pair<>(users.get(0), length);
        });
        addConverter(GuildUser.class, (user, shard, channel, guild, message, reaction, args) -> {
            Pair<User, Integer> pair = convert(User.class, user, shard, channel, guild, message, reaction, args);
            return new Pair<>(GuildUser.getGuildUser(guild, pair.getKey()), pair.getValue());
        }, ContextRequirement.GUILD);
        addConverter(Playlist.class, (user, shard, channel, guild, message, reaction, args) -> {
            args = args.toLowerCase();
            if (args.startsWith("global")) return new Pair<>(GlobalPlaylist.GLOBAL_PLAYLIST, args.equalsIgnoreCase("global playlist") ? 15 : 6);
            String[] split = args.split(" ");
            Pair<User, Integer> p = null;
            if (split.length > 1){
                try{p = InvocationObjectGetter.convert(User.class, user, null, null, guild, null, null, args);
                } catch (ArgumentException ignored){}
            }
            Pair<User, Integer> pair = p;
            if (pair != null){
                user = pair.getKey();
                args = args.substring(0, pair.getValue());
            }
            Pair<Guild, Integer> guildPair = null;
            if (split.length > 1){
                try{guildPair = InvocationObjectGetter.convert(Guild.class, user, null, null, guild, null, null, args);
                } catch (ArgumentException ignored){}
            }
            if (guildPair != null){
                guild = guildPair.getKey();
                args = args.substring(0, guildPair.getValue());
            }
            if (guild != null && (guildPair != null || split[0].equalsIgnoreCase("server") || split[0].equalsIgnoreCase("guild") || split[0].equalsIgnoreCase("s"))){
                if (ConfigHandler.getSetting(GuildPlaylistsConfig.class, guild).contains(split[1])){
                    return new Pair<>(Playlist.getPlaylist(guild, split[1]), split[0].length() + 1 + split[1].length());
                }
                throw new ArgumentException("There is no server playlist by that name");
            }
            args = args.toLowerCase().split(" ")[0];
            if (ConfigHandler.getSetting(UserPlaylistsConfig.class, user).contains(args)){
                return new Pair<>(Playlist.getPlaylist(user, args), (pair == null ? 0 : pair.getValue()) + args.length());
            }
            if (ConfigHandler.getSetting(GuildPlaylistsConfig.class, guild).contains(args)){
                return new Pair<>(Playlist.getPlaylist(guild, args), args.length());
            }
            String ar = args;
            AtomicReference<Pair<Playlist, Integer>> reference = new AtomicReference<>();
            if (guild != null) guild.getUsers().forEach(u -> {
                if (ConfigHandler.getSetting(UserPlaylistsConfig.class, u).contains(ar)){
                    if (reference.get() != null) throw new ArgumentException("Please specify a user, too many playlists are named that and owned by people in this server");
                    reference.set(new Pair<>(Playlist.getPlaylist(u, ar), (pair == null ? 0 : pair.getValue()) + ar.length()));
                }
            });
            throw new ArgumentException("Please specify a playlist by user and name or global");
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
                if (s == null) throw new ArgumentException("That shard may not does not exist, try a " + (i < 0 ? "higher" : "lower") + " number");
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
            if (role != null) return new Pair<>(role, arg.length());
            if (guild == null) throw new ArgumentException("You must be in a guild to use that command");
            if (arg.toLowerCase().equals("everyone")) return new Pair<>(guild.getEveryoneRole(), 8);
            for (Role r : message.getGuild().getRoles()){
                if (args.toLowerCase().startsWith(r.getName().toLowerCase()) && (args.length() == arg.length() || args.charAt(arg.length()) == ' ')){
                    if (role != null) throw new ArgumentException("To many roles named that, mention the role or provide an ID");
                    role = r;
                }
            }
            if (role == null) throw new ArgumentException("There are no roles by that name");
            return new Pair<>(role, role.getName().length());
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
                throw new ArgumentException("That is not a decimal number", e);
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
                if (!Configurable.class.isAssignableFrom(type) || Configurable.class.equals(type) || pair.get() != null) return;
                try{pair.set((Pair<Configurable, Integer>) converter.getKey().getObject(user, shard, channel, guild, message, reaction, args));
                } catch (Exception ignored){}
            });
            if (pair.get() == null) throw new ArgumentException("No configurable instance found");
            return pair.get();
        });
        addConverter(String.class, (invoker, shard, channel, guild, message, reaction, args) -> new Pair<>(args, args.length()));
        addConverter(AbstractCommand.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            Pair<AbstractCommand, String> command = CommandHandler.getMessageCommand(args);
            if (command == null) throw new ArgumentException("No such command found");
            return new Pair<>(command.getKey(), args.length() - command.getValue().length());
        });
        addConverter(Time.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            String first = args.split(" ")[0];
            return new Pair<>(new Time(first), first.length());
        });
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
        addConverter(Color.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            String[] strings = args.split(" ");
            try{return new Pair<>((Color) Color.class.getField(strings[0].toUpperCase()).get(null), strings[0].length());
            }catch(IllegalAccessException | NoSuchFieldException ignored) {}
            if (args.startsWith("#")) return new Pair<>(new Color(Integer.parseInt(strings[0].substring(1), 16), false), 7);
            Integer reserve = null;
            try {
                reserve = Integer.parseInt(strings[0].toUpperCase(), 16);
                if (reserve > 255) return new Pair<>(new Color(reserve), strings[0].length());
            } catch (Exception ignored){}
            if (strings.length > 2){
                try {
                    int[] vals = new int[3];
                    for (int i = 0; i < 3; i++) {
                        vals[i] = Integer.parseInt(strings[i]);
                    }
                    return new Pair<>(new Color(vals[0], vals[1], vals[2]), FormatHelper.lengthOf(strings, 3) + 2);
                } catch (Exception ignored){}
                try {
                    float[] vals = new float[3];
                    for (int i = 0; i < 3; i++) {
                        vals[i] = Float.parseFloat(strings[i]);
                    }
                    return new Pair<>(new Color(vals[0], vals[1], vals[2]), FormatHelper.lengthOf(strings, 3) + 2);
                } catch (Exception ignored){}
            }
            if (reserve != null) return new Pair<>(new Color(reserve), strings[0].length());
            throw new ArgumentException("Could not find color: for hex: insert a # in front | for rgb place the numbers without commas: r g b | for integer place the integer");
        });
        addConverter(Long.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            String[] strings = args.split(" ");
            try {
                long l = Long.parseLong(strings[0]);
                return new Pair<>(l, strings[0].length());
            } catch (NumberFormatException e) {throw new ArgumentException(e);}
        });
        addConverter(Track.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            List<Track> tracks = Track.getTracks(args);
            if (tracks.isEmpty()) throw new ArgumentException("No tracks found");
            return new Pair<>(tracks.get(0), args.length());// dangerous
        });
        addConverter(Team.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            Pair<User, Integer> userPair = null;
            try{userPair = convert(User.class, invoker, shard, channel, guild, message, reaction, args);
            } catch (ArgumentException ignored){}
            if (userPair != null) return new Pair<>(new Team(userPair.getKey()), userPair.getValue());
            Pair<Role, Integer> rolePair = null;
            try{rolePair = convert(Role.class, invoker, shard, channel, guild, message, reaction, args);
            } catch (ArgumentException ignored){}
            if (rolePair != null) return new Pair<>(new Team(.5F, rolePair.getKey()), rolePair.getValue());
            throw new ArgumentException("Please specify a team by role or user");
        });
        addConverter(ColorRange.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            List<Color> colors = new ArrayList<>(4);
            Pair<Color, Integer> pair;
            int total = 0, skipAmount;
            while (true){
                pair = convert(Color.class, invoker, shard, channel, guild, message, reaction, args);
                if (pair == null) return new Pair<>(new ColorRange(colors), total);
                else {
                    colors.add(pair.getKey());
                    total += pair.getValue();
                    args = args.substring(pair.getValue());
                    skipAmount = 0;
                    for (int i = 0; i < args.length(); i++) {
                        if (Character.isWhitespace(args.charAt(i))) ++skipAmount;
                        else break;
                    }
                    args = args.substring(skipAmount);
                    total += skipAmount;
                }
                if (total == 0) throw new ArgumentException("No colors specified to make color range");
            }
        });
        addConverter(Cryptocurrency.class, (invoker, shard, channel, guild, message, reaction, args) -> {
            String[] split = args.split(" ");
            StringBuilder total = new StringBuilder();
            Cryptocurrency cryptocurrency;
            for (int i = 0; i < 4; i++) {
                if (total.length() != 0) total.append(" ");
                total.append(split[i]);
                cryptocurrency = Cryptocurrency.getCryptocurrency(total.toString());
                if (cryptocurrency != null) return new Pair<>(cryptocurrency, total.length());
            }
            throw new ArgumentException("Could not identify Cryptocurrency");
        });
    }

    /**
     * Adds a conversion definition for a specified type.
     *
     * @param clazz the class type to convert part of a string to.
     * @param argumentConverter the function to define converting a command.
     * @param requirements enum representations of context requirements for requiring certain contexts in command invocation.
     * @param <T> the type to return.
     */
    private static <T> void addConverter(Class<T> clazz, ArgumentConverter<T> argumentConverter, ContextRequirement...requirements){
        EnumSet<ContextRequirement> req = EnumHelper.getSet(ContextRequirement.class, requirements);
        req.add(ContextRequirement.STRING);
        CONVERTER_MAP.put(clazz, new Pair<>(argumentConverter, req));
    }

    public static Set<ContextRequirement> getConvertRequirements(Class<?> type, ContextType contextType){
        return type.isEnum() ? Collections.emptySet() :  CONVERTER_MAP.get(type).getValue();
    }

    /**
     * Converts part of a string to a {@link Pair} whose
     * key is an object of type T and whose value is the
     * number of characters of the string used to define
     * the instance of the key.
     *
     * @param clazz the type of object to define.
     * @param user the invoking user.
     * @param shard the shard the invocation occurred on.
     * @param channel the channel a invocation occurred in.
     * @param guild the guild a command occurred in or null.
     * @param message the message the command occurred by or a message a reaction command was invoked on or null.
     * @param reaction the reaction the command occurred by or null.
     * @param args the arguments for a command.
     * @param <T> the type of object.
     * @return A {@link Pair} whose key is the derived object and the
     * @throws ArgumentException for when no instace was able to be derived from the given object
     */
    public static <T> Pair<T, Integer> convert(Class<T> clazz, User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args){
        if (args.equalsIgnoreCase("null")) return new Pair<>(null, 4);
        if (clazz.isEnum()) return (Pair<T, Integer>) EnumHelper.getValue(clazz, args);
        return (Pair<T, Integer>) CONVERTER_MAP.get(clazz).getKey().getObject(user, shard, channel, guild, message, reaction, args);
    }

    public static Set<Class<?>> getConversionTypes(){
        return CONVERTER_MAP.keySet();
    }

    /**
     * The function that defines converting
     * part of a string to an object of type E.
     *
     * @param <E> the type of key to return.
     */
    @FunctionalInterface
    private interface ArgumentConverter<E>{
        Pair<E, Integer> getObject(User invoker, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args);
    }

    /**
     * Forces the initialization of this class.
     */
    public static void initialize(){
        Log.log("Invocation Object Getter initialized");
    }

    /**
     * Process the argument input and converts it into an
     * expected object based on the given {@link Parameter}s
     * and places the converted object into the {@link Object[]}.
     *
     * @param command the command to fill command arguments for.
     * @param parameters the {@link Parameter}s for the command method's arguments.
     * @param objects the array to fill objects for the command's invocation.
     * @param user the invoking user.
     * @param shard the shard the invocation occurred on.
     * @param channel the channel a invocation occurred in.
     * @param guild the guild a command occurred in or null.
     * @param message the message the command occurred by or a message a reaction command was invoked on or null.
     * @param reaction the reaction the command occurred by or null.
     * @param args the arguments for a command.
     * @param argOverride overrides for argument invocation.
     * @return an array of objects for command parameters for the command's invocation.
     */
    public static Object[] replace(AbstractCommand command, Parameter[] parameters, Object[] objects, User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args, boolean[] argOverride){
        int commandArgIndex = 0;
        for (int i = 0; i < parameters.length; i++) {
            try {
                if (parameters[i].isAnnotationPresent(Context.class) || parameters[i].getAnnotations().length == 0){// null might be an instance of Context
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
                    Pair<Object, Integer> pair = convert((Class<Object>) parameters[i].getType(), user, shard, channel, guild, message, reaction, args);
                    objects[i] = pair.getKey();
                    int subSize = pair.getValue();
                    if (subSize > args.length()) {
                        Log.log("Size of reduction is linger then args: " + subSize + " for " + args);
                        subSize = args.length();
                    }else if (subSize < 0){
                        subSize = 0;
                        Log.log("Size of reduction is less than 0, " + subSize);
                    }
                    args = FormatHelper.trimFront(args.substring(subSize));
                }
            } catch (ArgumentException e){
                if (parameters[i].isAnnotationPresent(Argument.class) && parameters[i].getAnnotation(Argument.class).optional()){
                    if (parameters[i].getAnnotation(Argument.class).replacement() != ContextType.NONE) checkContextType(parameters[i].getType());
                    objects[i] = parameters[i].getAnnotation(Argument.class).replacement() == ContextType.NONE ? null : CONTEXT_MAP.get(parameters[i].getType()).get(parameters[i].getAnnotation(Argument.class).replacement()).getKey().getObject(user, shard, channel, guild, message, reaction, args);
                    continue;
                }
                e.setImproperUsage(commandArgIndex, parameters, command);
                throw e;
            }
        }
        return objects;
    }

    public static void checkConvertType(Class<?> type){
        if (type.isEnum()) return;
        for (Class<?> clazz : ReflectionHelper.getAssignableTypes(type)) if (CONVERTER_MAP.containsKey(clazz) && !clazz.isEnum()) return;
        throw new DevelopmentException("Can not convert objects of type: " + type.getSimpleName());
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
