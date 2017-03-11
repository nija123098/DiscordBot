package com.github.kaaz.discordbot.discordobjects.wrappers;

import com.github.kaaz.discordbot.config.ConfigLevel;
import com.github.kaaz.discordbot.config.Configurable;
import com.github.kaaz.discordbot.discordobjects.exception.WrapperHelper;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Channel implements Configurable {
    private static final Map<String, Channel> MAP = new ConcurrentHashMap<>();
    public static Channel getChannel(String id){// todo replace null
        return MAP.computeIfAbsent(id, s -> null);
    }
    static Channel getChannel(IChannel channel){
        return MAP.computeIfAbsent(channel.getID(), s -> new Channel(channel));
    }
    static List<Channel> getChannels(List<IChannel> iChannels){
        List<Channel> channels = new ArrayList<>(iChannels.size());
        iChannels.forEach(iChannel -> channels.add(getChannel(iChannel)));
        return channels;
    }
    public synchronized void update(IChannel channel){// hash is based on id, so no old channel is necessary
        MAP.get(channel.getID()).reference.set(channel);
    }
    private final AtomicReference<IChannel> reference;
    Channel(IChannel channel) {
        this.reference = new AtomicReference<>(channel);
    }
    IChannel channel(){
        return this.reference.get();
    }
    @Override
    public String getID() {
        return channel().getID();
    }

    public DiscordClient getClient() {
        return DiscordClient.get();
    }

    public Shard getShard() {
        return Shard.getShard(channel().getShard());
    }

    public Channel copy() {
        return Channel.getChannel(channel().copy());
    }

    public ConfigLevel getConfigLevel() {
        return ConfigLevel.CHANNEL;
    }

    public String getName() {
        return channel().getName();
    }

    public MessagesHistory getMessageHistory() {
        return new MessagesHistory(channel().getMessageHistory());
    }

    public MessagesHistory getMessageHistory(int i) {
        return new MessagesHistory(channel().getMessageHistory(i));
    }

    public MessagesHistory getMessageHistoryFrom(LocalDateTime localDateTime) {
        return new MessagesHistory(channel().getMessageHistoryFrom(localDateTime));
    }

    public MessagesHistory getMessageHistoryFrom(LocalDateTime localDateTime, int i) {
        return null;
    }

    public MessagesHistory getMessageHistoryTo(LocalDateTime localDateTime) {
        return null;
    }

    public MessagesHistory getMessageHistoryTo(LocalDateTime localDateTime, int i) {
        return null;
    }

    public MessagesHistory getMessageHistoryIn(LocalDateTime localDateTime, LocalDateTime localDateTime1) {
        return null;
    }

    public MessagesHistory getMessageHistoryIn(LocalDateTime localDateTime, LocalDateTime localDateTime1, int i) {
        return null;
    }

    public MessagesHistory getMessageHistoryFrom(String s) {
        return null;
    }

    public MessagesHistory getMessageHistoryFrom(String s, int i) {
        return null;
    }

    public MessagesHistory getMessageHistoryTo(String s) {
        return null;
    }

    public MessagesHistory getMessageHistoryTo(String s, int i) {
        return null;
    }

    public MessagesHistory getMessageHistoryIn(String s, String s1) {
        return null;
    }

    public MessagesHistory getMessageHistoryIn(String s, String s1, int i) {
        return null;
    }

    public MessagesHistory getFullMessageHistory() {
        return null;
    }

    public List<IMessage> bulkDelete() {
        return null;
    }

    public List<IMessage> bulkDelete(List<IMessage> list) {
        return null;
    }

    public int getMaxInternalCacheCount() {
        return 0;
    }

    public int getInternalCacheCount() {
        return 0;
    }

    public Message getMessageByID(String s) {
        return null;
    }

    public Guild getGuild() {
        return Guild.getGuild(channel().getGuild());
    }

    public boolean isPrivate() {
        return channel().isPrivate();
    }

    public String mention() {
        return channel().mention();
    }

    public String getTopic() {
        return channel().getTopic();
    }

    public IMessage sendMessage(String s) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IMessage sendMessage(EmbedObject embedObject) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IMessage sendMessage(String s, boolean b) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IMessage sendMessage(String s, EmbedObject embedObject) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IMessage sendMessage(String s, EmbedObject embedObject, boolean b) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IMessage sendFile(File file) throws FileNotFoundException, DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IMessage sendFile(String s, File file) throws FileNotFoundException, DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IMessage sendFile(EmbedObject embedObject, File file) throws FileNotFoundException, DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IMessage sendFile(String s, InputStream inputStream, String s1) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IMessage sendFile(EmbedObject embedObject, InputStream inputStream, String s) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IMessage sendFile(String s, boolean b, InputStream inputStream, String s1) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IMessage sendFile(String s, boolean b, InputStream inputStream, String s1, EmbedObject embedObject) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IMessage sendFile(MessageBuilder messageBuilder, InputStream inputStream, String s) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public void toggleTypingStatus() {
        channel().toggleTypingStatus();
    }

    public void setTypingStatus(boolean b) {
        channel().setTypingStatus(b);
    }

    public boolean getTypingStatus() {
        return channel().getTypingStatus();
    }

    public void edit(String s, int i, String s1) throws DiscordException, RateLimitException, MissingPermissionsException {
        WrapperHelper.wrap(() -> channel().edit(s, i, s1));
    }

    public void changeName(String s) {
        WrapperHelper.wrap(() -> channel().changeName(s));
    }

    public void changePosition(int i) {
        WrapperHelper.wrap(() -> channel().changePosition(i));
    }

    public void changeTopic(String s) {
        WrapperHelper.wrap(() -> channel().changeTopic(s));
    }

    public int getPosition() {
        return channel().getPosition();
    }

    public void delete() {
        WrapperHelper.wrap(() -> channel().delete());
    }

    public Map<User, PermOverride> getUserOverrides() {
        return PermOverride.getUserMap(channel().getUserOverrides());
    }

    public Map<Role, PermOverride> getRoleOverrides() {
        return PermOverride.getRoleMap(channel().getRoleOverrides());
    }

    public EnumSet<Permissions> getModifiedPermissions(IUser iUser) {
        return null;
    }

    public EnumSet<Permissions> getModifiedPermissions(IRole iRole) {
        return null;
    }

    public void removePermissionsOverride(IUser iUser) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void removePermissionsOverride(IRole iRole) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void overrideRolePermissions(IRole iRole, EnumSet<Permissions> enumSet, EnumSet<Permissions> enumSet1) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void overrideUserPermissions(IUser iUser, EnumSet<Permissions> enumSet, EnumSet<Permissions> enumSet1) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public List<User> getUsersHere() {
        return User.getUsers(channel().getUsersHere());
    }

    public List<Message> getPinnedMessages() {
        return WrapperHelper.wrap((WrapperHelper.Request<List<Message>>) () -> Message.getMessages(channel().getPinnedMessages()));
    }

    public void pin(Message message) {
        WrapperHelper.wrap(() -> channel().pin(message.message()));
    }

    public void unpin(Message message) {
        WrapperHelper.wrap(() -> channel().unpin(message.message()));
    }

    public boolean isDeleted() {
        return channel().isDeleted();
    }
}
