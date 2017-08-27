package com.github.kaaz.emily.discordobjects.wrappers;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.exception.ErrorWrapper;
import com.github.kaaz.emily.economy.configs.LastCookieUseConfig;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Time;
import sx.blah.discord.handle.obj.IUser;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class User implements Configurable {
    private static final Map<String, User> MAP = new MemoryManagementService.ManagedMap<>(180000);
    public static User getUser(String id){
        try {
            IUser user = DiscordClient.getAny(client -> client.getUserByID(FormatHelper.filtering(id, Character::isDigit)));
            if (user == null) return null;
            return MAP.computeIfAbsent(id, s -> new User(user));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static User getUser(IUser user){
        return MAP.computeIfAbsent(user.getStringID(), s -> new User(user));
    }
    static List<User> getUsers(List<IUser> iUsers){
        List<User> users = new ArrayList<>(iUsers.size());
        iUsers.forEach(iUser -> users.add(getUser(iUser)));
        return users;
    }
    public static void update(IUser user){// hash is based on id, so no old channel is necessary
        User u = MAP.get(user.getStringID());
        if (u != null){
            u.reference.set(user);
        }
    }
    private transient final AtomicReference<IUser> reference;
    private String ID;
    protected User() {
        this.reference = new AtomicReference<>(DiscordClient.getAny(client -> client.getUserByID(ID)));
    }
    User(IUser user) {
        this.registerExistence();
        this.reference = new AtomicReference<>(user);
        this.ID = this.reference.get().getStringID();
    }
    public IUser user(){
        return reference.get();
    }
    @Override
    public String getID() {
        return user().getStringID();
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.USER;
    }

    @Override
    public void checkPermissionToEdit(User user, Guild guild){
        if (!this.equals(user)){
            BotRole.BOT_ADMIN.checkRequiredRole(user, null);
        }
    }

    @Override
    public int hashCode() {
        return this.user().hashCode();
    }

    @Override
    public boolean equals(Object o){
        return o == this || o instanceof User && this.getID().equals(((User) o).getID());
    }

    public String getName() {
        return user().getName();
    }

    @Override
    public void manage() {// this should probably be more dependent on constants
        if (ConfigHandler.getSetting(LastCookieUseConfig.class, this) > System.currentTimeMillis() - 86_400_000){
            ConfigHandler.setSetting(LastCookieUseConfig.class, this, 0L);
        }
    }

    private transient final AtomicReference<Set<Guild>> guilds = new AtomicReference<>();
    public Set<Guild> getGuilds(){
        if (this.guilds.get() == null) {
            this.guilds.set(DiscordClient.getGuilds().stream().filter(guild -> guild.getUsers().contains(this)).collect(Collectors.toSet()));
            ScheduleService.schedule(120_000, () -> this.guilds.set(null));
        }
        return this.guilds.get();
    }

    public String getNameAndDiscrim(){
        return getName() + "#" + getDiscriminator();
    }

    public String getAvatar() {
        return user().getAvatar();
    }

    public String getAvatarURL() {
        return user().getAvatarURL();
    }

    public long getJoinDate(){
        return Time.toMillis(this.user().getCreationDate());
    }

    public Presence getPresence() {
        return Presence.getPresence(user().getPresence());
    }

    public String getDisplayName(Guild guild) {
        return user().getDisplayName(guild.guild());
    }

    public String getNickname(Guild guild) {
        String nick = this.getDisplayName(guild);
        return this.getName().equals(nick) ? null : nick;
    }

    public String mention() {
        return mention(true);
    }

    public String mention(boolean mentionWithNickname) {
        return user().mention(mentionWithNickname);
    }

    public String getDiscriminator() {
        return user().getDiscriminator();
    }

    public List<Role> getRolesForGuild(Guild guild) {
        return Role.getRoles(user().getRolesForGuild(guild.guild()));
    }

    public EnumSet<DiscordPermission> getPermissionsForGuild(Guild guild) {
        return DiscordPermission.getDiscordPermissions(user().getPermissionsForGuild(guild.guild()));
    }

    public boolean isBot() {
        return user().isBot();
    }

    public VoiceChannel getConnectedVoiceChannel(Guild guild){
        return VoiceChannel.getVoiceChannel(this.user().getVoiceStateForGuild(guild.guild()).getChannel());
    }

    public void moveToVoiceChannel(VoiceChannel newChannel) {
        ErrorWrapper.wrap(() -> user().moveToVoiceChannel(newChannel.channel()));
    }

    public DirectChannel getOrCreatePMChannel() {
        return ErrorWrapper.wrap((ErrorWrapper.Request<DirectChannel>) () -> DirectChannel.getDirectChannel(user().getOrCreatePMChannel()));
    }

    public boolean isDeaf(Guild guild) {
        return user().getVoiceStateForGuild(guild.guild()).isDeafened();
    }

    public boolean isMuted(Guild guild) {
        return user().getVoiceStateForGuild(guild.guild()).isMuted();
    }

    public void addRole(Role role) {
        ErrorWrapper.wrap(() -> user().addRole(role.role()));
    }

    public void removeRole(Role role) {
        ErrorWrapper.wrap(() -> user().removeRole(role.role()));
    }
}
