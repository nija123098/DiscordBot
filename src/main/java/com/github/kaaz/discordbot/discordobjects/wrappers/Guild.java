package com.github.kaaz.discordbot.discordobjects.wrappers;

import com.github.kaaz.discordbot.config.ConfigLevel;
import com.github.kaaz.discordbot.config.Configurable;
import com.github.kaaz.discordbot.discordobjects.exception.WrapperHelper;
import sx.blah.discord.handle.audio.IAudioManager;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Guild implements Configurable {
    private static final Map<String, Guild> MAP = new ConcurrentHashMap<>();
    public static Guild getGuild(String id){// todo replace null
        return MAP.computeIfAbsent(id, s -> null);
    }
    static Guild getGuild(IGuild guild){
        return MAP.computeIfAbsent(guild.getID(), s -> new Guild(guild));
    }
    public synchronized void update(IGuild guild){// hash is based on id, so no old channel is necessary
        MAP.get(guild.getID()).reference.set(guild);
    }
    private final AtomicReference<IGuild> reference;
    private Guild(IGuild guild) {
        this.reference = new AtomicReference<>(guild);
    }
    IGuild guild(){
        return this.reference.get();
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.GUILD;
    }
    @Override
    public String getID() {
        return guild().getID();
    }
    // THE FOLLOWING ARE WRAPPER METHODS
    public DiscordClient getClient() {
        return DiscordClient.get();
    }

    public Shard getShard() {
        return Shard.getShard(guild().getShard());
    }

    public String getOwnerID() {
        return guild().getOwnerID();
    }

    public User getOwner() {
        return User.getUser(guild().getOwnerID());
    }

    public String getIcon() {
        return guild().getIcon();
    }

    public String getIconURL() {
        return guild().getIconURL();
    }

    public List<Channel> getChannels() {
        return Channel.getChannels(guild().getChannels());
    }

    public Channel getChannelByID(String s) {
        return Channel.getChannel(s);
    }

    public List<User> getUsers() {
        return User.getUsers(guild().getUsers());
    }

    public User getUserByID(String s) {
        return User.getUser(s);
    }

    public List<Channel> getChannelsByName(String s) {
        return Channel.getChannels(guild().getChannelsByName(s));
    }

    public List<VoiceChannel> getVoiceChannelsByName(String s) {
        return VoiceChannel.getVoiceChannels(guild().getVoiceChannelsByName(s));
    }

    public List<User> getUsersByName(String s) {
        return getUsersByName(s, true);
    }

    public List<User> getUsersByName(String s, boolean b) {
        return User.getUsers(guild().getUsersByName(s, b));
    }

    public List<User> getUsersByRole(Role role) {
        return User.getUsers(guild().getUsersByRole(role.role()));
    }

    public String getName() {
        return guild().getName();
    }

    public List<Role> getRoles() {
        return Role.getRoles(guild().getRoles());
    }

    public List<Role> getRolesForUser(User user) {
        return Role.getRoles(guild().getRolesForUser(user.user()));
    }

    public Role getRoleByID(String s) {
        return Role.getRole(s);
    }

    public List<Role> getRolesByName(String s) {
        return Role.getRoles(guild().getRolesByName(s));
    }

    public List<VoiceChannel> getVoiceChannels() {
        return VoiceChannel.getVoiceChannels(guild().getVoiceChannels());
    }

    public VoiceChannel getVoiceChannelByID(String s) {
        return VoiceChannel.getVoiceChannel(s);
    }

    public VoiceChannel getConnectedVoiceChannel() {
        return VoiceChannel.getVoiceChannel(guild().getConnectedVoiceChannel());
    }

    public VoiceChannel getAFKChannel() {
        return VoiceChannel.getVoiceChannel(guild().getAFKChannel());
    }

    public int getAFKTimeout() {
        return guild().getAFKTimeout();
    }

    public Role createRole() {
        return WrapperHelper.wrap((WrapperHelper.Request<Role>) () -> Role.getRole(guild().createRole()));
    }

    public List<User> getBannedUsers() {
        return WrapperHelper.wrap((WrapperHelper.Request<List<User>>) () -> User.getUsers(guild().getBannedUsers()));
    }

    public void banUser(User user) {
        WrapperHelper.wrap(() -> guild().banUser(user.user()));
    }

    public void banUser(User user, int i) {
        WrapperHelper.wrap(() -> guild().banUser(user.user(), i));
    }

    public void banUser(String s) {
        WrapperHelper.wrap(() -> guild().banUser(s));
    }

    public void banUser(String s, int i) {
        WrapperHelper.wrap(() -> guild().banUser(s, i));
    }

    public void pardonUser(String s) {
        WrapperHelper.wrap(() -> guild().pardonUser(s));
    }

    public void kickUser(User user) {
        WrapperHelper.wrap(() -> guild().kickUser(user.user()));
    }

    public void editUserRoles(User user, Role...roles) {
        List<IRole> iRoles = new ArrayList<>(roles.length);
        for (Role role : roles) {
            iRoles.add(role.role());
        }
        WrapperHelper.wrap(() -> guild().editUserRoles(user.user(), Role.getRoles(iRoles).toArray(new IRole[roles.length])));
    }

    public void setDeafenUser(User user, boolean b) {
        WrapperHelper.wrap(() -> guild().setDeafenUser(user.user(), b));
    }

    public void setMuteUser(User user, boolean b) {
        WrapperHelper.wrap(() -> guild().setMuteUser(user.user(), b));
    }

    public void setUserNickname(User user, String s) {
        WrapperHelper.wrap(() -> guild().setUserNickname(user.user(), s));
    }

    public void changeName(String s) {
        WrapperHelper.wrap(() -> guild().changeName(s));
    }

    public void changeRegion(Region region) {
        WrapperHelper.wrap(() -> Region.getRegion(guild().getRegion()));
    }

    public void changeAFKChannel(VoiceChannel voiceChannel) {
        WrapperHelper.wrap(() -> guild().changeAFKChannel(voiceChannel.channel()));
    }

    public void changeAFKTimeout(int i) {
        WrapperHelper.wrap(() -> guild().changeAFKTimeout(i));
    }

    public void leave() {
        WrapperHelper.wrap(() -> guild().leave());
    }

    public Channel createChannel(String s) {
        return WrapperHelper.wrap((WrapperHelper.Request<Channel>) () -> Channel.getChannel(guild().createChannel(s)));
    }

    public VoiceChannel createVoiceChannel(String s) {
        return WrapperHelper.wrap((WrapperHelper.Request<VoiceChannel>) () -> VoiceChannel.getVoiceChannel(guild().createVoiceChannel(s)));
    }

    public Region getRegion() {
        return Region.getRegion(guild().getRegion());
    }

    public Role getEveryoneRole() {
        return WrapperHelper.wrap((WrapperHelper.Request<Role>) () -> Role.getRole(guild().getEveryoneRole()));
    }

    public Channel getGeneralChannel() {
        return Channel.getChannel(guild().getGeneralChannel());
    }

    public void reorderRoles(Role...roles) {
        List<IRole> iRoles = new ArrayList<>(roles.length);
        for (Role role : roles) {
            iRoles.add(role.role());
        }
        WrapperHelper.wrap(() -> guild().reorderRoles(iRoles.toArray(new IRole[roles.length])));
    }

    public int getUsersToBePruned(int i) {
        return WrapperHelper.wrap((WrapperHelper.Request<Integer>) () -> guild().getUsersToBePruned(i));
    }

    public int pruneUsers(int i) {
        return WrapperHelper.wrap((WrapperHelper.Request<Integer>) () -> guild().pruneUsers(i));
    }

    public boolean isDeleted() {
        return guild().isDeleted();
    }

    public IAudioManager getAudioManager() {
        return null;
    }

    public LocalDateTime getJoinTimeForUser(User user) {
        return guild().getJoinTimeForUser(user.user());
    }

    public Message getMessageByID(String s) {
        return Message.getMessage(s);
    }

    public int getTotalMemberCount() {
        return guild().getTotalMemberCount();
    }
}
