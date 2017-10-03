package com.github.nija123098.evelyn.discordobjects.wrappers;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.cache.LongMap;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Wraps a Discord4j {@link IChannel.PermissionOverride} object.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class PermOverride {// no need to store this, it doesn't get used right now
    static Map<User, PermOverride> getUserMap(LongMap<IChannel.PermissionOverride> overrideMap){
        HashMap<User, PermOverride> map = new HashMap<>(overrideMap.size());
        overrideMap.forEach((user, permOverride) -> map.put(User.getUser(String.valueOf(user)), get(permOverride)));
        return map;
    }
    static Map<Role, PermOverride> getRoleMap(LongMap<IChannel.PermissionOverride> overrideMap){
        HashMap<Role, PermOverride> map = new HashMap<>(overrideMap.size());
        overrideMap.forEach((role, permOverride) -> map.put(Role.getRole(String.valueOf(role)), get(permOverride)));
        return map;
    }
    static PermOverride get(EnumSet<Permissions> allow, EnumSet<Permissions> deny){
        return new PermOverride(allow, deny);
    }
    static PermOverride get(IChannel.PermissionOverride permissionOverride){
        return new PermOverride(permissionOverride.allow(), permissionOverride.deny());
    }
    private final EnumSet<Permissions> allow;
    private final EnumSet<Permissions> deny;
    private PermOverride(EnumSet<Permissions> allow, EnumSet<Permissions> deny) {
        this.allow = allow;
        this.deny = deny;
    }
    public EnumSet<Permissions> allow() {
        return this.allow;
    }
    public EnumSet<Permissions> deny() {
        return this.deny;
    }
}
