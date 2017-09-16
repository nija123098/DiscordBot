package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.config.ConfigLevel;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.exception.ErrorWrapper;
import com.github.nija123098.evelyn.exeption.ConfigurableConvertException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.GetterUtil;
import com.github.nija123098.evelyn.util.Time;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IVoiceChannel;

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
        String r = FormatHelper.filtering(id, Character::isLetterOrDigit);
        try {
            Channel channel = getChannel((IChannel) GetterUtil.getAny(DiscordClient.clients(), f -> f.getChannelByID(r)));
            return channel == null ? VoiceChannel.getVoiceChannel(r) : channel;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Channel getChannel(IChannel channel){
        if (channel == null) return null;
        return MAP.computeIfAbsent(channel.getStringID(), s -> channel.isPrivate() ? new DirectChannel((IPrivateChannel) channel) : channel instanceof IVoiceChannel ? new VoiceChannel((IVoiceChannel) channel) : new Channel(channel));
    }
    static List<Channel> getChannels(List<IChannel> iChannels){
        List<Channel> channels = new ArrayList<>(iChannels.size());
        iChannels.forEach(iChannel -> channels.add(getChannel(iChannel)));
        return channels;
    }
    public static void update(IChannel channel){// should handle DirectChannel and VoiceChannel updates as well
        Channel c = MAP.get(channel.getStringID());
        if (c != null){
            c.reference.set(channel);
        }
    }
    final transient AtomicReference<IChannel> reference;
    private String ID;
    protected Channel() {
        this.reference = new AtomicReference<>(DiscordClient.getAny(client -> client.getChannelByID(ID)));
    }
    Channel(IChannel channel) {
        this.reference = new AtomicReference<>(channel);
        this.ID = channel.getID();
        this.registerExistence();
    }
    public IChannel channel(){
        return this.reference.get();
    }
    @Override
    public String getID() {
        return channel().getStringID();
    }
    @Override
    public void checkPermissionToEdit(User user, Guild guild){
        BotRole.GUILD_TRUSTEE.checkRequiredRole(user, guild);
    }

    @Override
    public boolean shouldCache() {
        return false;
    }

    @Override
    public Configurable getGoverningObject(){
        return isPrivate() ? GlobalConfigurable.GLOBAL : this.getGuild();
    }

    @Override
    public <T extends Configurable> Configurable convert(Class<T> t) {
        if (Channel.class.isAssignableFrom(t)) return this;
        if (!this.isPrivate() && this.getGuild().getGeneralChannel().equals(this)){
            if (t.equals(Guild.class)) return this.getGuild();
            if (t.equals(Role.class)) return this.getGuild().getEveryoneRole();
        }
        throw new ConfigurableConvertException(this.getClass(), t);
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Channel && ((Channel) o).getID().equals(this.getID());
    }

    @Override
    public int hashCode() {
        return this.channel().hashCode();
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
        if (channel().isPrivate()) return null;
        return Guild.getGuild(channel().getGuild());
    }

    public boolean isPrivate() {
        return channel().isPrivate();
    }

    public boolean isNSFW(){
        return channel().isNSFW();
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

    public List<Message> getMessages(){
        return Message.getMessages(this.channel().getMessages());
    }

    public List<Message> getMessages(int count){
        return Message.getMessages(channel().getMessageHistory(count));
    }

    public List<Message> getMessagesTo(long date){
        return Message.getMessages(channel().getMessageHistoryTo(Time.toLocalDateTime(date)));
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
