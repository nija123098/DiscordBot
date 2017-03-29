package com.github.kaaz.emily.discordobjects.wrappers;

import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.exception.ErrorWrapper;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import sx.blah.discord.handle.obj.IChannel;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Channel implements Configurable {
    private static final Map<String, Channel> MAP = new MemoryManagementService.ManagedMap<>(180000);
    public static Channel getChannel(String id){
        return getChannel(DiscordClient.client().getChannelByID(id));
    }
    static Channel getChannel(IChannel channel){
        return MAP.computeIfAbsent(channel.getID(), s -> new Channel(channel));
    }
    static List<Channel> getChannels(List<IChannel> iChannels){
        List<Channel> channels = new ArrayList<>(iChannels.size());
        iChannels.forEach(iChannel -> channels.add(getChannel(iChannel)));
        return channels;
    }
    public static void update(IChannel channel){// should handle DirectChannel and VoiceChannel updates as well
        MAP.get(channel.getID()).reference.set(channel);
    }
    final AtomicReference<IChannel> reference;
    Channel(IChannel channel) {
        this.reference = new AtomicReference<>(channel);
    }
    public IChannel channel(){
        return this.reference.get();
    }
    @Override
    public String getID() {
        return channel().getID();
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

    public List<Message> bulkDelete() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<List<Message>>) () -> Message.getMessages(channel().bulkDelete()));
    }

    public List<Message> bulkDelete(List<Message> list) {
        return ErrorWrapper.wrap((ErrorWrapper.Request<List<Message>>) () -> Message.getMessages(channel().bulkDelete(Message.getIMessages(list))));
    }

    public Message getMessageByID(String s) {
        return Message.getMessage(s);
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

    public void toggleTypingStatus() {
        channel().toggleTypingStatus();
    }

    public void setTypingStatus(boolean b) {
        ErrorWrapper.wrap(() -> channel().setTypingStatus(b));
    }

    public boolean getTypingStatus() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<Boolean>) () -> channel().getTypingStatus());
    }

    public void edit(String s, int i, String s1) {
        ErrorWrapper.wrap(() -> channel().edit(s, i, s1));
    }

    public void changeName(String s) {
        ErrorWrapper.wrap(() -> channel().changeName(s));
    }

    public void changePosition(int i) {
        ErrorWrapper.wrap(() -> channel().changePosition(i));
    }

    public void changeTopic(String s) {
        ErrorWrapper.wrap(() -> channel().changeTopic(s));
    }

    public int getPosition() {
        return channel().getPosition();
    }

    public void delete() {
        ErrorWrapper.wrap(() -> channel().delete());
    }

    public Map<User, PermOverride> getUserOverrides() {
        return PermOverride.getUserMap(channel().getUserOverrides());
    }

    public Map<Role, PermOverride> getRoleOverrides() {
        return PermOverride.getRoleMap(channel().getRoleOverrides());
    }

    public EnumSet<DiscordPermission> getModifiedPermissions(User user) {
        return DiscordPermission.getDiscordPermissions(channel().getModifiedPermissions(user.user()));
    }

    public EnumSet<DiscordPermission> getModifiedPermissions(Role role) {
        return DiscordPermission.getDiscordPermissions(channel().getModifiedPermissions(role.role()));
    }

    public void removePermissionsOverride(User user) {
        ErrorWrapper.wrap(() -> channel().removePermissionsOverride(user.user()));
    }

    public void removePermissionsOverride(Role role) {
        ErrorWrapper.wrap(() -> channel().removePermissionsOverride(role.role()));
    }

    public void overrideRolePermissions(Role role, EnumSet<DiscordPermission> enumSet, EnumSet<DiscordPermission> enumSet1) {
        ErrorWrapper.wrap(() -> channel().overrideRolePermissions(role.role(), DiscordPermission.getPermissions(enumSet), DiscordPermission.getPermissions(enumSet1)));
    }

    public void overrideUserPermissions(User user, EnumSet<DiscordPermission> enumSet, EnumSet<DiscordPermission> enumSet1) {
        ErrorWrapper.wrap(() -> channel().overrideUserPermissions(user.user(), DiscordPermission.getPermissions(enumSet), DiscordPermission.getPermissions(enumSet1)));
    }

    public List<User> getUsersHere() {
        return User.getUsers(channel().getUsersHere());
    }

    public List<Message> getPinnedMessages() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<List<Message>>) () -> Message.getMessages(channel().getPinnedMessages()));
    }

    public void pin(Message message) {
        ErrorWrapper.wrap(() -> channel().pin(message.message()));
    }

    public void unpin(Message message) {
        ErrorWrapper.wrap(() -> channel().unpin(message.message()));
    }

    public boolean isDeleted() {
        return channel().isDeleted();
    }
}
