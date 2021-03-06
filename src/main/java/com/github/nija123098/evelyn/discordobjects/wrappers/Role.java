package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.config.ConfigLevel;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.ExceptionWrapper;
import com.github.nija123098.evelyn.exception.ConfigurableConvertException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.ConcurrentLoadingHashMap;
import com.github.nija123098.evelyn.util.FormatHelper;
import sx.blah.discord.handle.obj.IRole;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Wraps a Discord4j {@link IRole} object.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class Role implements Configurable {
    private static final ConcurrentLoadingHashMap<IRole, Role> CACHE = new ConcurrentLoadingHashMap<>(ConfigProvider.CACHE_SETTINGS.roleSize(), Role::new);
    public static Role getRole(String id) {
        try {
            IRole iRole = DiscordClient.getAny(client -> client.getRoleByID(Long.parseLong(FormatHelper.filtering(id, Character::isLetterOrDigit))));
            if (iRole == null) return null;
            return CACHE.get(iRole);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Role getRole(IRole iRole) {
        return CACHE.get(iRole);
    }
    public static List<Role> getRoles(List<IRole> iRoles) {
        List<Role> roles = new ArrayList<>(iRoles.size());
        iRoles.forEach(iUser -> roles.add(getRole(iUser)));
        return roles;
    }
    public static void update(IRole iRole) {// hash is based on id, so no old channel is necessary
        Role r = CACHE.getIfPresent(iRole);
        if (r != null) r.reference.set(iRole);
    }
    private transient AtomicReference<IRole> reference;
    private String ID;
    public Role() {
        this.reference = new AtomicReference<>(DiscordClient.getAny(client -> client.getRoleByID(Long.parseLong(ID))));
    }
    private Role(IRole role) {
        this.reference = new AtomicReference<>(role);
        this.ID = role.getStringID();
        this.registerExistence();
    }
    IRole role() {
        if (this.reference == null) this.reference = new AtomicReference<>(DiscordClient.getAny(client -> client.getRoleByID(Long.parseLong(ID))));
        return this.reference.get();
    }

    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.ROLE;
    }

    @Override
    public void checkPermissionToEdit(User user, Guild guild) {
        BotRole.GUILD_TRUSTEE.checkRequiredRole(user, guild);
    }

    @Override
    public Configurable getGoverningObject() {
        return getGuild();
    }

    @Override
    public <T extends Configurable> Configurable convert(Class<T> t) {
        if (t.equals(Role.class)) return this;
        if (this.getGuild().getEveryoneRole().equals(this)) {
            if (t.equals(Guild.class)) return this.getGuild();
            if (t.equals(Channel.class) && this.getGuild().getGeneralChannel() != null) return this.getGuild().getGeneralChannel();
        }
        throw new ConfigurableConvertException(this.getClass(), t);
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Role && ((Role) o).getID().equals(this.getID());
    }

    @Override
    public int hashCode() {
        return this.role().hashCode();
    }

    public List<User> getUsers() {
        Guild guild = this.getGuild();
        return guild.getUsers().stream().filter(user -> user.getRolesForGuild(guild).contains(this)).collect(Collectors.toList());
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
        ExceptionWrapper.wrap(() -> role().edit(color, hoist, name, DiscordPermission.getPermissions(permissions), isMentionable));
    }

    public void changeColor(Color color) {
        ExceptionWrapper.wrap(() -> role().changeColor(color));
    }

    public void changeHoist(boolean hoist) {
        ExceptionWrapper.wrap(() -> role().changeHoist(hoist));
    }

    public void changeName(String name) {
        ExceptionWrapper.wrap(() -> role().changeName(name));
    }

    public void changePermissions(EnumSet<DiscordPermission> permissions) {
        ExceptionWrapper.wrap(() -> role().changePermissions(DiscordPermission.getPermissions(permissions)));
    }

    public void changeMentionable(boolean isMentionable) {
        ExceptionWrapper.wrap(() -> role().changeMentionable(isMentionable));
    }

    public void delete() {
        ExceptionWrapper.wrap(() -> role().delete());
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
        return role().getStringID();
    }

    public Shard getShard() {
        return Shard.getShard(role().getShard());
    }
}
