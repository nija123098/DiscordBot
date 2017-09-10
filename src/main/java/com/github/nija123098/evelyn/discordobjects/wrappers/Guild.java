package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.config.ConfigLevel;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.exception.ErrorWrapper;
import com.github.nija123098.evelyn.exeption.ConfigurableConvertException;
import com.github.nija123098.evelyn.exeption.GhostException;
import com.github.nija123098.evelyn.launcher.BotConfig;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import com.github.nija123098.evelyn.util.Time;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Guild implements Configurable {
    private static final Map<String, Guild> MAP = new MemoryManagementService.ManagedMap<>(180000);
    public static Guild getGuild(String id){
        try {
            IGuild guild = DiscordClient.getAny(client -> client.getGuildByID(id));
            if (guild == null) return null;
            return MAP.computeIfAbsent(id, s -> new Guild(guild));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Guild getGuild(IGuild guild){
        if (guild == null){
            return null;
        }
        return MAP.computeIfAbsent(guild.getStringID(), s -> new Guild(guild));
    }
    static List<Guild> getGuilds(List<IGuild> iGuilds){
        List<Guild> list = new ArrayList<>(iGuilds.size());
        iGuilds.forEach(guild -> list.add(getGuild(guild)));
        return list;
    }
    public static void update(IGuild guild){// hash is based on id, so no old channel is necessary
        Guild g = MAP.get(guild.getStringID());
        if (g != null){
            g.reference.set(guild);
        }
    }
    private String ID;
    private transient final AtomicReference<IGuild> reference;
    private Guild(IGuild guild) {
        this.reference = new AtomicReference<>(guild);
        this.ID = guild.getID();
        this.registerExistence();
    }
    public Guild() {
        this.reference = new AtomicReference<>(DiscordClient.getAny(client -> client.getGuildByID(ID)));
    }
    public IGuild guild(){
        return this.reference.get();
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.GUILD;
    }
    @Override
    public String getID() {
        return guild().getStringID();
    }

    @Override
    public boolean shouldCashe() {
        return false;
    }

    public void checkPermissionToEdit(User user, Guild guild){
        BotRole.GUILD_TRUSTEE.checkRequiredRole(user, guild);
    }

    @Override
    public <T extends Configurable> Configurable convert(Class<T> t) {
        if (t.equals(Guild.class)) return this;
        if (t.equals(Channel.class)) return this.getGeneralChannel();
        if (t.equals(Role.class)) return this.getEveryoneRole();
        throw new ConfigurableConvertException(this.getClass(), t);
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Guild && ((Guild) o).getID().equals(this.getID());
    }

    @Override
    public int hashCode() {
        return this.guild().hashCode();
    }

    // THE FOLLOWING ARE WRAPPER METHODS
    public Shard getShard() {
        return Shard.getShard(guild().getShard());
    }

    public String getOwnerID() {
        return guild().getOwnerID();
    }

    public User getOwner() {
        return User.getUser(guild().getOwner());
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

    public int getUserSize(){
        return guild().getUsers().size();
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
        if (s == null || s.isEmpty()) return Collections.emptyList();
        AtomicReference<String> nick = new AtomicReference<>();
        return this.getUsers().stream().filter(user -> {
            if (user.getName().equals(s)) return true;
            if (!b) return false;
            nick.set(user.getDisplayName(this));
            return !nick.get().equals(user.getName()) && nick.get().equals(s);
        }).collect(Collectors.toList());
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
        return ErrorWrapper.wrap((ErrorWrapper.Request<Role>) () -> Role.getRole(guild().createRole()));
    }

    public List<User> getBannedUsers() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<List<User>>) () -> User.getUsers(guild().getBannedUsers()));
    }

    public void banUser(User user) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().banUser(user.user()));
    }

    public void banUser(User user, int i) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().banUser(user.user(), i));
    }

    public void banUser(String s) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().banUser(s));
    }

    public void banUser(String s, int i) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().banUser(s, i));
    }

    public void pardonUser(String s) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().pardonUser(s));
    }

    public void kickUser(User user) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().kickUser(user.user()));
    }

    public void editUserRoles(User user, Role...roles) {
        if (BotConfig.GHOST_MODE) return;
        List<IRole> iRoles = new ArrayList<>(roles.length);
        for (Role role : roles) {
            iRoles.add(role.role());
        }
        ErrorWrapper.wrap(() -> guild().editUserRoles(user.user(), Role.getRoles(iRoles).toArray(new IRole[roles.length])));
    }

    public void setDeafenUser(User user, boolean b) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().setDeafenUser(user.user(), b));
    }

    public void setMuteUser(User user, boolean b) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().setMuteUser(user.user(), b));
    }

    public void setUserNickname(User user, String s) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().setUserNickname(user.user(), s));
    }

    public void changeName(String s) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().changeName(s));
    }

    public void changeRegion(Region region) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> Region.getRegion(guild().getRegion()));
    }

    public void changeAFKChannel(VoiceChannel voiceChannel) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().changeAFKChannel(voiceChannel.channel()));
    }

    public void changeAFKTimeout(int i) {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().changeAFKTimeout(i));
    }

    public void leave() {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> guild().leave());
    }

    public Channel createChannel(String s) {
        if (BotConfig.GHOST_MODE) throw new GhostException();
        return ErrorWrapper.wrap((ErrorWrapper.Request<Channel>) () -> Channel.getChannel(guild().createChannel(s)));
    }

    public VoiceChannel createVoiceChannel(String s) {
        if (BotConfig.GHOST_MODE) throw new GhostException();
        return ErrorWrapper.wrap((ErrorWrapper.Request<VoiceChannel>) () -> VoiceChannel.getVoiceChannel(guild().createVoiceChannel(s)));
    }

    public Region getRegion() {
        return Region.getRegion(guild().getRegion());
    }

    public Role getEveryoneRole() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<Role>) () -> Role.getRole(guild().getEveryoneRole()));
    }

    public Channel getGeneralChannel() {
        return Channel.getChannel(guild().getGeneralChannel());
    }

    public void reorderRoles(Role...roles) {
        if (BotConfig.GHOST_MODE) return;
        List<IRole> iRoles = new ArrayList<>(roles.length);
        for (Role role : roles) {
            iRoles.add(role.role());
        }
        ErrorWrapper.wrap(() -> guild().reorderRoles(iRoles.toArray(new IRole[roles.length])));
    }

    public int getUsersToBePruned(int i) {
        return ErrorWrapper.wrap((ErrorWrapper.Request<Integer>) () -> guild().getUsersToBePruned(i));
    }

    public int pruneUsers(int i) {
        return ErrorWrapper.wrap((ErrorWrapper.Request<Integer>) () -> guild().pruneUsers(i));
    }

    public boolean isDeleted() {
        return guild().isDeleted();
    }

    public long getJoinTimeForUser(User user) {
        return Time.toMillis(guild().getJoinTimeForUser(user.user()));
    }

    public Message getMessageByID(String s) {
        return Message.getMessage(s);
    }

    public int getTotalMemberCount() {
        return guild().getTotalMemberCount();
    }

    public long getCreationDate() {
        return Time.toMillis(guild().getCreationDate());
    }
}
