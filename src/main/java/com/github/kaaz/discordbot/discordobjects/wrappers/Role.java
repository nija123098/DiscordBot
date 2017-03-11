package com.github.kaaz.discordbot.discordobjects.wrappers;

import sx.blah.discord.handle.obj.IRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 3/7/2017.
 */
public class Role {
    private static final Map<String, Role> MAP = new ConcurrentHashMap<>();
    public static Role getRole(String id){// todo replace null
        return MAP.computeIfAbsent(id, s -> null);
    }
    static Role getRole(IRole guild){
        return MAP.computeIfAbsent(guild.getID(), s -> new Role(guild));
    }
    public void update(IRole guild){// hash is based on id, so no old channel is necessary
        MAP.get(guild.getID()).reference.set(guild);
    }
    private final AtomicReference<IRole> reference;
    private Role(IRole guild) {
        this.reference = new AtomicReference<>(guild);
    }
    IRole role(){
        return this.reference.get();
    }
    public static List<Role> getRoles(List<IRole> iRoles) {
        List<Role> roles = new ArrayList<>(iRoles.size());
        iRoles.forEach(iUser -> roles.add(getRole(iUser)));
        return roles;
    }
}
