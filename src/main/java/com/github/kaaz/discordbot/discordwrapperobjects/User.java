package com.github.kaaz.discordbot.discordwrapperobjects;

import com.github.kaaz.discordbot.config.ConfigLevel;
import com.github.kaaz.discordbot.config.Configurable;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class User implements Configurable {// todo rewrite to completely match necessary Discord stuff
    private static final Map<String, User> MAP = new ConcurrentHashMap<>();
    public static User getUser(String id){// todo replace null
        return MAP.computeIfAbsent(id, null);
    }
    static User getUser(IUser user){
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
        return null;
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.USER;
    }
    public boolean isBot() {
        return reference.get().isBot();
    }
}
