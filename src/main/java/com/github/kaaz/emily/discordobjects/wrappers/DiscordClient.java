package com.github.kaaz.emily.discordobjects.wrappers;

import com.github.kaaz.emily.discordobjects.exception.ErrorWrapper;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IUser;

import java.util.*;
import java.util.function.Function;

/**
 * Made by nija123098 on 3/7/2017.
 */
public class DiscordClient {
    private static List<IDiscordClient> clients;
    private static Map<Shard, IDiscordClient> clientMap;
    public static void set(List<IDiscordClient> discordClients){
        clients = discordClients;
        clientMap = new HashMap<>(clients.size() + 1, 1);
    }
    public static void load(){
        clients.forEach(client -> clientMap.put(Shard.getShard(client.getShards().get(0)), client));
    }
    public static List<IDiscordClient> clients(){
        return clients;
    }
    public static IDiscordClient getClientForShard(Shard shard){
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
        clients.forEach(client -> ErrorWrapper.wrap(client::login));
    }

    public static void logout() {
        clients.forEach(client -> ErrorWrapper.wrap(client::logout));
    }

    public static void changeUsername(String username) {
        clients.forEach(client -> ErrorWrapper.wrap(() -> client.changeUsername(username)));
    }

    public static void changePlayingText(String playingText) {
        clients.forEach(client -> ErrorWrapper.wrap(() -> client.changePlayingText(playingText)));
    }

    public static void online(String playingText) {
        clients.forEach(client -> ErrorWrapper.wrap(() -> client.online(playingText)));
    }

    public static void online() {
        clients.forEach(client -> ErrorWrapper.wrap(client::online));
    }

    public static void idle(String playingText) {
        clients.forEach(client -> ErrorWrapper.wrap(() -> client.idle(playingText)));
    }

    public static void idle() {
        clients.forEach(client -> ErrorWrapper.wrap(client::idle));
    }

    public static void streaming(String playingText, String streamingUrl) {
        clients.forEach(client -> ErrorWrapper.wrap(() -> client.streaming(playingText, streamingUrl)));
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
        return getAll(client -> ErrorWrapper.wrap((ErrorWrapper.Request<List<Region>>) () -> Region.getRegions(client.getRegions())));
    }

    public static List<VoiceChannel> getConnectedVoiceChannels() {
        return getAll(client -> VoiceChannel.getVoiceChannels(client.getConnectedVoiceChannels()));
    }

    public static String getApplicationDescription() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<String>) () -> clients.get(0).getApplicationDescription());
    }

    public static String getApplicationIconURL() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<String>) () -> clients.get(0).getApplicationIconURL());
    }

    public static String getApplicationClientID() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<String>) () -> clients.get(0).getApplicationClientID());
    }

    public static String getApplicationName() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<String>) () -> clients.get(0).getApplicationName());
    }

    private static User owner;
    public static User getApplicationOwner() {
        return owner == null ? (owner = ErrorWrapper.wrap((ErrorWrapper.Request<User>) () -> User.getUser(clients.get(0).getApplicationOwner()))) : owner;
    }

    public static <E> E getAny(Function<IDiscordClient, E> function){
        E e;
        for (IDiscordClient f : clients) if ((e = function.apply(f)) != null) return e;
        return null;
    }

    public static <E> List<E> getAll(Function<IDiscordClient, Collection<E>> function){
        List<E> list = new ArrayList<>();
        clients().forEach(f -> list.addAll(function.apply(f)));
        return list;
    }
}
