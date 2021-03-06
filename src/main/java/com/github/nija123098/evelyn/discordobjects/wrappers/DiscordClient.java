package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.discordobjects.ExceptionWrapper;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;

import java.util.*;
import java.util.function.Function;

/**
 * Wraps the Discord4J {@link IDiscordClient} class for general user controls.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordClient {
    private static List<IDiscordClient> clients;
    private static Map<Shard, IDiscordClient> clientMap;
    public static void set(List<IDiscordClient> discordClients) {
        clients = discordClients;
        clientMap = new HashMap<>(clients.size() + 1, 1);
    }
    public static void load() {
        clients.forEach(client -> clientMap.put(Shard.getShard(client.getShards().get(0)), client));
    }
    public static List<IDiscordClient> clients() {
        return clients;
    }
    public static IDiscordClient getClientForShard(Shard shard) {
        return clientMap.get(shard);
    }
    // WRAPPING METHODS
    public static List<Shard> getShards() {
        List<Shard> shards = new ArrayList<>(clients.size());
        clients.forEach(client -> shards.addAll(Shard.getShards(client.getShards())));
        return shards;
    }

    public static int getShardCount() {
        return clients.size();
    }

    public static void login() {
        clients.forEach(client -> ExceptionWrapper.wrap(client::login));
    }

    public static void logout() {
        clients.forEach(client -> ExceptionWrapper.wrap(client::logout));
    }

    public static void changeUsername(String username) {
        clients.forEach(client -> ExceptionWrapper.wrap(() -> client.changeUsername(username)));
    }

    public static void changePresence(String text, String stream) {
        clients().forEach(iDiscordClient -> ExceptionWrapper.wrap(() -> iDiscordClient.changeStreamingPresence(StatusType.ONLINE, text, stream)));
    }

    public static void changePresence(Presence.Status status, Presence.Activity activity, String text) {
        clients().forEach(iDiscordClient -> ExceptionWrapper.wrap(() -> iDiscordClient.changePresence(status.convert(), activity.convert(), text)));
    }


    public static boolean isReady() {
        for (IDiscordClient client : clients) if (!client.isReady()) return false;
        return true;
    }

    public static boolean isLoggedIn() {
        for (IDiscordClient client : clients) if (!client.isLoggedIn()) return false;
        return true;
    }

    public static User getOurUser() {
        return User.getUser(clients.get(0).getOurUser());
    }

    public static Guild getSupportServer() {
        return getGuildByID(ConfigProvider.BOT_SETTINGS.supportServerId());
    }

    public static List<Channel> getChannels(boolean includePrivate) {
        return getAll(client -> Channel.getChannels(client.getChannels()));
    }

    public static List<Channel> getChannels() {
        return getChannels(true);
    }

    public static List<VoiceChannel> getVoiceChannels() {
        return getAll(client -> VoiceChannel.getVoiceChannels(client.getConnectedVoiceChannels()));
    }

    public static List<Guild> getGuilds() {
        return getAll(client -> Guild.getGuilds(client.getGuilds()));
    }

    public static Guild getGuildByID(String guildID) {
        return Guild.getGuild(guildID);
    }

    public static List<User> getUsers() {
        Set<IUser> users = new HashSet<>(1000000, 1);
        clients().forEach(client -> users.addAll(client.getUsers()));
        return User.getUsers(users);
    }

    public static List<Role> getRoles() {
        return getAll(client -> Role.getRoles(client.getRoles()));
    }

    public static Role getRoleByID(String roleID) {
        return Role.getRole(roleID);
    }

    public static Message getMessageByID(String messageID) {
        return Message.getMessage(messageID);
    }

    public static List<Region> getRegions() {
        return getAll(client -> ExceptionWrapper.wrap((ExceptionWrapper.Request<List<Region>>) () -> Region.getRegions(client.getRegions())));
    }

    public static List<VoiceChannel> getConnectedVoiceChannels() {
        return getAll(client -> VoiceChannel.getVoiceChannels(client.getConnectedVoiceChannels()));
    }

    public static String getApplicationDescription() {
        return ExceptionWrapper.wrap((ExceptionWrapper.Request<String>) () -> clients.get(0).getApplicationDescription());
    }

    public static String getApplicationIconURL() {
        return ExceptionWrapper.wrap((ExceptionWrapper.Request<String>) () -> clients.get(0).getApplicationIconURL());
    }

    public static String getApplicationClientID() {
        return ExceptionWrapper.wrap((ExceptionWrapper.Request<String>) () -> clients.get(0).getApplicationClientID());
    }

    public static String getApplicationName() {
        return ExceptionWrapper.wrap((ExceptionWrapper.Request<String>) () -> clients.get(0).getApplicationName());
    }

    private static User owner;
    public static User getApplicationOwner() {
        return owner == null ? (owner = ExceptionWrapper.wrap((ExceptionWrapper.Request<User>) () -> User.getUser(clients.get(0).getApplicationOwner()))) : owner;
    }

    public static <E> E getAny(Function<IDiscordClient, E> function) {
        E e;
        for (IDiscordClient f : clients) if ((e = function.apply(f)) != null) return e;
        return null;
    }

    public static <E> List<E> getAll(Function<IDiscordClient, Collection<E>> function) {
        List<E> list = new ArrayList<>();
        clients().forEach(f -> list.addAll(function.apply(f)));
        return list;
    }
}
