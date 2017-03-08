package com.github.kaaz.discordbot.discordwrapperobjects;

import com.github.kaaz.discordbot.config.ConfigLevel;
import com.github.kaaz.discordbot.config.Configurable;
import sx.blah.discord.handle.audio.IAudioManager;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.Image;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Guild implements Configurable {// todo rewrite to completely match necessary Discord stuff
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
        return null;
    }
    @Override
    public String getID() {
        return guild().getID();
    }
    // THE FOLLOWING ARE MIMIC METHODS
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
        List<Channel> channels = new ArrayList<>();
        guild().getChannels().forEach(iChannel -> channels.add(Channel.getChannel(iChannel)));
        return channels;
    }

    public Channel getChannelByID(String s) {
        return Channel.getChannel(s);
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        guild().getUsers().forEach(iUser -> users.add(User.getUser(iUser)));
        return users;
    }

    public User getUserByID(String s) {
        return User.getUser(s);
    }

    public List<Channel> getChannelsByName(String s) {
        List<Channel> channels = new ArrayList<>();
        guild().getChannelsByName(s).forEach(iChannel -> channels.add(Channel.getChannel(iChannel)));
        return channels;
    }

    public List<VoiceChannel> getVoiceChannelsByName(String s) {
        List<VoiceChannel> channels = new ArrayList<>();
        guild().getVoiceChannelsByName(s).forEach(iChannel -> channels.add(VoiceChannel.getVoiceChannel(iChannel)));
        return channels;
    }

    public List<User> getUsersByName(String s) {
        return getUsersByName(s, true);
    }

    public List<User> getUsersByName(String s, boolean b) {
        List<User> users = new ArrayList<>();
        guild().getUsersByName(s, b).forEach(iUser -> users.add(User.getUser(iUser)));
        return users;
    }

    public List<User> getUsersByRole(IRole iRole) {
        List<User> users = new ArrayList<>();
        guild().getUsersByRole(iRole).forEach(iUser -> users.add(User.getUser(iUser)));
        return users;
    }

    public String getName() {
        return guild().getName();
    }

    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        guild().getRoles().forEach(iRole -> roles.add(Role.getRole(iRole)));
        return roles;
    }

    public List<Role> getRolesForUser(User user) {
        List<Role> roles = new ArrayList<>();
        guild().getRolesForUser(user.user.get()).forEach(iRole -> roles.add(Role.getRole(iRole)));
        return roles;
    }

    public Role getRoleByID(String s) {
        return Role.getRole(s);
    }

    public List<Role> getRolesByName(String s) {
        return null;
    }

    public List<IVoiceChannel> getVoiceChannels() {
        return null;
    }

    public IVoiceChannel getVoiceChannelByID(String s) {
        return null;
    }

    public IVoiceChannel getConnectedVoiceChannel() {
        return null;
    }

    public IVoiceChannel getAFKChannel() {
        return null;
    }

    public int getAFKTimeout() {
        return 0;
    }

    public IRole createRole() throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public List<IUser> getBannedUsers() throws DiscordException, RateLimitException {
        return null;
    }

    public void banUser(IUser iUser) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void banUser(IUser iUser, int i) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void banUser(String s) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void banUser(String s, int i) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void pardonUser(String s) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void kickUser(IUser iUser) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void editUserRoles(IUser iUser, IRole[] iRoles) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void setDeafenUser(IUser iUser, boolean b) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void setMuteUser(IUser iUser, boolean b) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void setUserNickname(IUser iUser, String s) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void edit(String s, IRegion iRegion, VerificationLevel verificationLevel, Image image, IVoiceChannel iVoiceChannel, int i) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void changeName(String s) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void changeRegion(IRegion iRegion) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void changeVerificationLevel(VerificationLevel verificationLevel) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void changeIcon(Image image) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void changeAFKChannel(IVoiceChannel iVoiceChannel) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void changeAFKTimeout(int i) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void deleteGuild() throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public void leaveGuild() throws DiscordException, RateLimitException {

    }

    public void leave() throws DiscordException, RateLimitException {

    }

    public IChannel createChannel(String s) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IVoiceChannel createVoiceChannel(String s) throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public IRegion getRegion() {
        return null;
    }

    public VerificationLevel getVerificationLevel() {
        return null;
    }

    public IRole getEveryoneRole() {
        return null;
    }

    public IChannel getGeneralChannel() {
        return null;
    }

    public List<IInvite> getInvites() throws DiscordException, RateLimitException, MissingPermissionsException {
        return null;
    }

    public void reorderRoles(IRole... iRoles) throws DiscordException, RateLimitException, MissingPermissionsException {

    }

    public int getUsersToBePruned(int i) throws DiscordException, RateLimitException {
        return 0;
    }

    public int pruneUsers(int i) throws DiscordException, RateLimitException {
        return 0;
    }

    public boolean isDeleted() {
        return false;
    }

    public IAudioManager getAudioManager() {
        return null;
    }

    public LocalDateTime getJoinTimeForUser(IUser iUser) throws DiscordException {
        return null;
    }

    public IMessage getMessageByID(String s) {
        return null;
    }

    public List<IEmoji> getEmojis() {
        return null;
    }

    public IEmoji getEmojiByID(String s) {
        return null;
    }

    public IEmoji getEmojiByName(String s) {
        return null;
    }

    public IWebhook getWebhookByID(String s) {
        return null;
    }

    public List<IWebhook> getWebhooksByName(String s) {
        return null;
    }

    public List<IWebhook> getWebhooks() {
        return null;
    }

    public int getTotalMemberCount() {
        return 0;
    }
    /*
    @Override
    public String getID() {
        return null;
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.GUILD;
    }
    public User getOwner() {
        return new User(channel.get().getOwner());// temp
    }
    public EnumSet<DiscordPermission> getPermissionsForGuild(User user){
        return EnumSet.allOf(DiscordPermission.class);// temp
    }
    */
}
