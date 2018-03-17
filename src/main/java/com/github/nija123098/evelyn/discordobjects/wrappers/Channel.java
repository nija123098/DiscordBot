package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.config.ConfigLevel;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.ExceptionWrapper;
import com.github.nija123098.evelyn.exception.ConfigurableConvertException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.CacheHelper;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.GetterUtil;
import com.github.nija123098.evelyn.util.Time;
import com.google.common.cache.LoadingCache;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Wraps a Discord4J {@link IChannel} object.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class Channel implements Configurable {
    private static final LoadingCache<IChannel, Channel> CACHE = CacheHelper.getLoadingCache(Runtime.getRuntime().availableProcessors() * 2, ConfigProvider.CACHE_SETTINGS.channelSize(), 300_000, channel -> channel.isPrivate() ? new DirectChannel((IPrivateChannel) channel) : channel instanceof IVoiceChannel ? new VoiceChannel((IVoiceChannel) channel) : new Channel(channel));
    public static Channel getChannel(String id) {
        try {
            Long longId = Long.parseLong(FormatHelper.filtering(id, Character::isLetterOrDigit));
            IChannel channel = GetterUtil.getAny(DiscordClient.clients(), f -> f.getChannelByID(longId));
            if (channel == null) {
                channel = GetterUtil.getAny(DiscordClient.clients(), f -> f.getVoiceChannelByID(longId));
                if (channel == null) return null;
                return VoiceChannel.getVoiceChannel((IVoiceChannel) channel);
            }
            return CACHE.getUnchecked(channel);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Channel getChannel(IChannel channel) {
        if (channel == null) return null;
        return CACHE.getUnchecked(channel);
    }
    static List<Channel> getChannels(List<IChannel> iChannels) {
        List<Channel> channels = new ArrayList<>(iChannels.size());
        iChannels.forEach(iChannel -> channels.add(getChannel(iChannel)));
        return channels;
    }
    public static void update(IChannel channel) {// should handle DirectChannel and VoiceChannel updates as well
        Channel c = CACHE.getIfPresent(channel);
        if (c != null) c.reference.set(channel);
    }
    final transient AtomicReference<IChannel> reference;
    private String ID;
    protected Channel() {
        this.reference = new AtomicReference<>(DiscordClient.getAny(client -> client.getChannelByID(Long.parseLong(ID))));
    }
    Channel(IChannel channel) {
        this.reference = new AtomicReference<>(channel);
        this.ID = channel.getStringID();
        this.registerExistence();
    }
    public IChannel channel() {
        return this.reference.get();
    }
    @Override
    public String getID() {
        return channel().getStringID();
    }
    @Override
    public void checkPermissionToEdit(User user, Guild guild) {
        BotRole.GUILD_TRUSTEE.checkRequiredRole(user, guild);
    }

    @Override
    public Configurable getGoverningObject() {
        return isPrivate() ? GlobalConfigurable.GLOBAL : this.getGuild();
    }

    @Override
    public <T extends Configurable> Configurable convert(Class<T> t) {
        if (Channel.class.isAssignableFrom(t)) return this;
        if (!this.isPrivate() && this.equals(this.getGuild().getGeneralChannel())) {
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
        return ExceptionWrapper.wrap((ExceptionWrapper.Request<List<Message>>) () -> Message.getMessages(channel().bulkDelete()));
    }

    public List<Message> bulkDelete(List<Message> list) {
        return ExceptionWrapper.wrap((ExceptionWrapper.Request<List<Message>>) () -> Message.getMessages(channel().bulkDelete(Message.getIMessages(list))));
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

    public boolean isNSFW() {
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
        ExceptionWrapper.wrap(() -> channel().setTypingStatus(b));
    }

    public boolean getTypingStatus() {
        return ExceptionWrapper.wrap((ExceptionWrapper.Request<Boolean>) () -> channel().getTypingStatus());
    }

    public void edit(String s, int i, String s1) {
        ExceptionWrapper.wrap(() -> channel().edit(s, i, s1));
    }

    public void changeName(String s) {
        ExceptionWrapper.wrap(() -> channel().changeName(s));
    }

    public void changePosition(int i) {
        ExceptionWrapper.wrap(() -> channel().changePosition(i));
    }

    public void changeTopic(String s) {
        ExceptionWrapper.wrap(() -> channel().changeTopic(s));
    }

    public int getPosition() {
        return channel().getPosition();
    }

    public void delete() {
        ExceptionWrapper.wrap(() -> channel().delete());
    }

    public Map<User, PermOverride> getUserOverrides() {
        return PermOverride.getUserMap(channel().getRoleOverrides());
    }

    public Map<Role, PermOverride> getRoleOverrides() {
        return PermOverride.getRoleMap(channel().getUserOverrides());
    }

    public EnumSet<DiscordPermission> getModifiedPermissions(User user) {
        return DiscordPermission.getDiscordPermissions(channel().getModifiedPermissions(user.user()));
    }

    public EnumSet<DiscordPermission> getModifiedPermissions(Role role) {
        return DiscordPermission.getDiscordPermissions(channel().getModifiedPermissions(role.role()));
    }

    public void removePermissionsOverride(User user) {
        ExceptionWrapper.wrap(() -> channel().removePermissionsOverride(user.user()));
    }

    public void removePermissionsOverride(Role role) {
        ExceptionWrapper.wrap(() -> channel().removePermissionsOverride(role.role()));
    }

    public void overrideRolePermissions(Role role, EnumSet<DiscordPermission> enumSet, EnumSet<DiscordPermission> enumSet1) {
        ExceptionWrapper.wrap(() -> channel().overrideRolePermissions(role.role(), DiscordPermission.getPermissions(enumSet), DiscordPermission.getPermissions(enumSet1)));
    }

    public void overrideUserPermissions(User user, EnumSet<DiscordPermission> enumSet, EnumSet<DiscordPermission> enumSet1) {
        ExceptionWrapper.wrap(() -> channel().overrideUserPermissions(user.user(), DiscordPermission.getPermissions(enumSet), DiscordPermission.getPermissions(enumSet1)));
    }

    public List<User> getUsersHere() {
        return User.getUsers(channel().getUsersHere());
    }

    public List<Message> getPinnedMessages() {
        return ExceptionWrapper.wrap((ExceptionWrapper.Request<List<Message>>) () -> Message.getMessages(channel().getPinnedMessages()));
    }

    public List<Message> getMessages() {
        return Message.getMessages(Arrays.asList(this.channel().getMessageHistoryTo(this.channel().getCreationDate()).asArray()));
    }

    public List<Message> getMessages(int count) {
        return Message.getMessages(channel().getMessageHistory(count));
    }

    public List<Message> getMessagesTo(long date) {
        return Message.getMessages(channel().getMessageHistoryTo(Time.toInstant(date)));
    }

    public void pin(Message message) {
        ExceptionWrapper.wrap(() -> channel().pin(message.message()));
    }

    public void unpin(Message message) {
        ExceptionWrapper.wrap(() -> channel().unpin(message.message()));
    }

    public boolean isDeleted() {
        return channel().isDeleted();
    }

    public Category getCategory() {
        return Category.getCategory(channel().getCategory());
    }

    public boolean canPost(User user) {
        return DiscordPermission.hasChannelPermissions(user, this, DiscordPermission.READ_MESSAGES, DiscordPermission.READ_MESSAGE_HISTORY, DiscordPermission.SEND_MESSAGES);
    }

    public boolean canPost() {
        return DiscordPermission.hasChannelPermissions(DiscordClient.getOurUser(), this, DiscordPermission.READ_MESSAGES, DiscordPermission.READ_MESSAGE_HISTORY, DiscordPermission.SEND_MESSAGES);
    }
    public void changeCatagory(Category category) {
        ExceptionWrapper.wrap(() -> this.channel().changeCategory(category.category()));
    }
}
