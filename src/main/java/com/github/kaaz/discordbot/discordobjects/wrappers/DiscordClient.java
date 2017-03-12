package com.github.kaaz.discordbot.discordobjects.wrappers;

import com.github.kaaz.discordbot.discordobjects.exception.ErrorWrapper;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

import java.util.List;

/**
 * Made by nija123098 on 3/7/2017.
 */
public class DiscordClient {
    private static IDiscordClient client;
    private static DiscordClient wrapper;
    public static DiscordClient get(){
        return wrapper;
    }
    public static void set(IDiscordClient discordClient){
        client = discordClient;
        wrapper = new DiscordClient();
    }
    IDiscordClient client(){
        return client;
    }
    // WRAPPING METHODS
    public EventDispatcher getDispatcher() {
        return null;
    }

    public List<Shard> getShards() {
        return Shard.getShards(client.getShards());
    }

    public int getShardCount() {
        return client.getShardCount();
    }

    public void login() {
        ErrorWrapper.wrap(() -> client.login());
    }

    public void logout() {
        ErrorWrapper.wrap(() -> client.logout());
    }

    public void changeUsername(String username) {
        client.changeUsername(username);
    }

    public void changePlayingText(String playingText) {
        client.changePlayingText(playingText);
    }

    public void online(String playingText) {
        client.online(playingText);
    }

    public void online() {
        client.online();
    }

    public void idle(String playingText) {
        client.idle(playingText);
    }

    public void idle() {
        client.idle();
    }

    public void streaming(String playingText, String streamingUrl) {
        client.streaming(playingText, streamingUrl);
    }

    public boolean isReady() {
        return client.isReady();
    }

    public boolean isLoggedIn() {
        return client.isLoggedIn();
    }

    public User getOurUser() {
        return User.getUser(client.getOurUser());
    }

    public List<Channel> getChannels(boolean includePrivate) {
        return Channel.getChannels(client.getChannels(includePrivate));
    }

    public List<Channel> getChannels() {
        return getChannels(true);
    }

    public Channel getChannelByID(String channelID) {
        return Channel.getChannel(channelID);
    }

    public List<VoiceChannel> getVoiceChannels() {
        return VoiceChannel.getVoiceChannels(client.getVoiceChannels());
    }

    public VoiceChannel getVoiceChannelByID(String id) {
        return VoiceChannel.getVoiceChannel(id);
    }

    public List<Guild> getGuilds() {
        return Guild.getGuilds(client.getGuilds());
    }

    public Guild getGuildByID(String guildID) {
        return Guild.getGuild(guildID);
    }

    public List<User> getUsers() {
        return User.getUsers(client.getUsers());
    }

    public User getUserByID(String userID) {
        return User.getUser(userID);
    }

    public List<Role> getRoles() {
        return Role.getRoles(client.getRoles());
    }

    public Role getRoleByID(String roleID) {
        return Role.getRole(roleID);
    }

    public Message getMessageByID(String messageID) {
        return Message.getMessage(messageID);
    }

    public DirectChannel getOrCreatePMChannel(User user) {
        return user.getOrCreatePMChannel();
    }

    public List<Region> getRegions() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<List<Region>>) () -> Region.getRegions(client.getRegions()));
    }

    public Region getRegionByID(String regionID) {
        return Region.getRegion(regionID);
    }

    public List<VoiceChannel> getConnectedVoiceChannels() {
        return VoiceChannel.getVoiceChannels(client.getConnectedVoiceChannels());
    }

    public String getApplicationDescription() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<String>) () -> client.getApplicationDescription());
    }

    public String getApplicationIconURL() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<String>) () -> client.getApplicationIconURL());
    }

    public String getApplicationClientID() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<String>) () -> client.getApplicationClientID());
    }

    public String getApplicationName() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<String>) () -> client.getApplicationName());
    }

    public User getApplicationOwner() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<User>) () -> User.getUser(client.getApplicationOwner()));
    }
}
