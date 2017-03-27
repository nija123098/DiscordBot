package com.github.kaaz.emily.discordobjects.wrappers;

import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.exception.ErrorWrapper;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class User implements Configurable<User> {
    private static final Map<String, User> MAP = new MemoryManagementService.ManagedMap<>(180000);
    static User getUser(String id){
        return MAP.computeIfAbsent(id, s -> new User(DiscordClient.client().getUserByID(id)));
    }
    public static User getUser(IUser user){
        return MAP.computeIfAbsent(user.getID(), s -> new User(user));
    }
    static List<User> getUsers(List<IUser> iUsers){
        List<User> users = new ArrayList<>(iUsers.size());
        iUsers.forEach(iUser -> users.add(getUser(iUser)));
        return users;
    }
    public static void update(IUser user){// hash is based on id, so no old channel is necessary
        MAP.get(user.getID()).reference.set(user);
    }
    private final AtomicReference<IUser> reference;
    IUser user(){
        return reference.get();
    }
    User(IUser user) {
        this.reference = new AtomicReference<>(user);
    }
    @Override
    public String getID() {
        return user().getID();
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.USER;
    }

    public boolean equals(Object o){
        return o instanceof User && this.getID().equals(((User) o).getID());
    }

    public String getName() {
        return user().getName();
    }

    public String getAvatar() {
        return user().getAvatar();
    }

    public String getAvatarURL() {
        return user().getAvatarURL();
    }

    public Presence getPresence() {
        return Presence.getPresence(user().getPresence());
    }

    public String getDisplayName(Guild guild) {
        return user().getDisplayName(guild.guild());
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
