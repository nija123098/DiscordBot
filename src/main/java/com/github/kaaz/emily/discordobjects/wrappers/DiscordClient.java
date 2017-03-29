package com.github.kaaz.emily.discordobjects.wrappers;

import com.github.kaaz.emily.discordobjects.exception.ErrorWrapper;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import sx.blah.discord.api.IDiscordClient;

import java.util.List;

/**
 * Made by nija123098 on 3/7/2017.
 */
public class DiscordClient {
    private static IDiscordClient client;
    public static void set(IDiscordClient discordClient){
        client = discordClient;
        client.getDispatcher().registerListener(EventDistributor.class);
    }
    public static IDiscordClient client(){
        return client;
    }
    // WRAPPING METHODS
    public static List<Shard> getShards() {
        return Shard.getShards(client.getShards());
    }

    public static int getShardCount() {
        return client.getShardCount();
    }

    public static void login() {
        ErrorWrapper.wrap(() -> client.login());
    }

    public static void logout() {
        ErrorWrapper.wrap(() -> client.logout());
    }

    public static void changeUsername(String username) {
        client.changeUsername(username);
    }

    public static void changePlayingText(String playingText) {
        client.changePlayingText(playingText);
    }

    public static void online(String playingText) {
        client.online(playingText);
    }

    public static void online() {
        client.online();
    }

    public static void idle(String playingText) {
        client.idle(playingText);
    }

    public static void idle() {
        client.idle();
    }

    public static void streaming(String playingText, String streamingUrl) {
        client.streaming(playingText, streamingUrl);
    }

    public static boolean isReady() {
        return client.isReady();
    }

    public static boolean isLoggedIn() {
        return client.isLoggedIn();
    }

    public static User getOurUser() {
        return User.getUser(client.getOurUser());
    }

    public static List<Channel> getChannels(boolean includePrivate) {
        return Channel.getChannels(client.getChannels(includePrivate));
    }

    public static List<Channel> getChannels() {
        return getChannels(true);
    }

    public static Channel getChannelByID(String channelID) {
        return Channel.getChannel(channelID);
    }

    public static List<VoiceChannel> getVoiceChannels() {
        return VoiceChannel.getVoiceChannels(client.getVoiceChannels());
    }

    public static VoiceChannel getVoiceChannelByID(String id) {
        return VoiceChannel.getVoiceChannel(id);
    }

    public static List<Guild> getGuilds() {
        return Guild.getGuilds(client.getGuilds());
    }

    public static Guild getGuildByID(String guildID) {
        return Guild.getGuild(guildID);
    }

    public static List<User> getUsers() {
        return User.getUsers(client.getUsers());
    }

    public static User getUserByID(String userID) {
        return User.getUser(userID);
    }

    public static List<Role> getRoles() {
        return Role.getRoles(client.getRoles());
    }

    public static Role getRoleByID(String roleID) {
        return Role.getRole(roleID);
    }

    public static Message getMessageByID(String messageID) {
        return Message.getMessage(messageID);
    }

    public static DirectChannel getOrCreatePMChannel(User user) {
        return user.getOrCreatePMChannel();
    }

    public static List<Region> getRegions() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<List<Region>>) () -> Region.getRegions(client.getRegions()));
    }

    public static Region getRegionByID(String regionID) {
        return Region.getRegion(regionID);
    }

    public static List<VoiceChannel> getConnectedVoiceChannels() {
        return VoiceChannel.getVoiceChannels(client.getConnectedVoiceChannels());
    }

    public static String getApplicationDescription() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<String>) () -> client.getApplicationDescription());
    }

    public static String getApplicationIconURL() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<String>) () -> client.getApplicationIconURL());
    }

    public static String getApplicationClientID() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<String>) () -> client.getApplicationClientID());
    }

    public static String getApplicationName() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<String>) () -> client.getApplicationName());
    }

    public static User getApplicationOwner() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<User>) () -> User.getUser(client.getApplicationOwner()));
    }
}
