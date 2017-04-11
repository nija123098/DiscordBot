package com.github.kaaz.emily.discordobjects.wrappers;

import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.exception.ErrorWrapper;
import com.github.kaaz.emily.perms.BotRole;
import sx.blah.discord.handle.obj.IRole;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 3/7/2017.
 */
public class Role implements Configurable{
    private static final Map<String, Role> MAP = new ConcurrentHashMap<>();
    public static Role getRole(String id){
        IRole role = DiscordClient.client().getRoleByID(id);
        if (role == null){
            return null;
        }
        return getRole(role);
    }
    static Role getRole(IRole guild){
        return MAP.computeIfAbsent(guild.getID(), s -> new Role(guild));
    }
    public static java.util.List<Role> getRoles(java.util.List<IRole> iRoles) {
        java.util.List<Role> roles = new ArrayList<>(iRoles.size());
        iRoles.forEach(iUser -> roles.add(getRole(iUser)));
        return roles;
    }
    public static void update(IRole guild){// hash is based on id, so no old channel is necessary
        MAP.get(guild.getID()).reference.set(guild);
    }
    private final AtomicReference<IRole> reference;
    private Role(IRole guild) {
        this.reference = new AtomicReference<>(guild);
    }
    IRole role(){
        return this.reference.get();
    }

    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.ROLE;
    }

    @Override
    public void checkPermissionToEdit(User user, Guild guild){
        BotRole.checkRequiredRole(BotRole.GUILD_TRUSTEE, user, guild);
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Role && ((Role) o).getID().equals(this.getID());
    }

    @Override
    public int hashCode() {
        return this.getID().hashCode();
    }

    //WRAPPER METHODS
    public int getPosition() {
        return role().getPosition();
    }

    public EnumSet<DiscordPermission> getPermissions() {
        return DiscordPermission.getDiscordPermissions(role().getPermissions());
    }

    public String getName() {
        return role().getName();
    }

    public boolean isManaged() {
        return role().isManaged();
    }

    public boolean isHoisted() {
        return role().isHoisted();
    }

    public Color getColor() {
        return role().getColor();
    }

    public boolean isMentionable() {
        return role().isMentionable();
    }

    public Guild getGuild() {
        return Guild.getGuild(role().getGuild());
    }

    public void edit(Color color, boolean hoist, String name, EnumSet<DiscordPermission> permissions, boolean isMentionable) {
        ErrorWrapper.wrap(() -> role().edit(color, hoist, name, DiscordPermission.getPermissions(permissions), isMentionable));
    }

    public void changeColor(Color color) {
        ErrorWrapper.wrap(() -> role().changeColor(color));
    }

    public void changeHoist(boolean hoist) {
        ErrorWrapper.wrap(() -> role().changeHoist(hoist));
    }

    public void changeName(String name) {
        ErrorWrapper.wrap(() -> role().changeName(name));
    }

    public void changePermissions(EnumSet<DiscordPermission> permissions) {
        ErrorWrapper.wrap(() -> role().changePermissions(DiscordPermission.getPermissions(permissions)));
    }

    public void changeMentionable(boolean isMentionable) {
        ErrorWrapper.wrap(() -> role().changeMentionable(isMentionable));
    }

    public void delete() {
        ErrorWrapper.wrap(() -> role().delete());
    }

    public boolean isEveryoneRole() {
        return role().isEveryoneRole();
    }

    public boolean isDeleted() {
        return role().isDeleted();
    }

    public String mention() {
        return role().mention();
    }

    public String getID() {
        return role().getID();
    }

    public Shard getShard() {
        return Shard.getShard(role().getShard());
    }
}
