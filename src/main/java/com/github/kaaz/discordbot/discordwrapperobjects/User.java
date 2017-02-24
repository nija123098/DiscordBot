package com.github.kaaz.discordbot.discordwrapperobjects;

import com.github.kaaz.discordbot.config.ConfigLevel;
import com.github.kaaz.discordbot.config.Configurable;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class User implements Configurable {// todo rewrite
    private static final List<User> USERS = new ArrayList<>();
    public static synchronized User getGuild(IUser user){// haven't thought this out quite all the way through
        for (int i = 0; i < USERS.size(); i++) {
            if (USERS.get(i).user.equals(user)){
                return USERS.get(i);
            }
        }
        User g = new User(user);
        USERS.add(g);
        return g;
    }
    IUser user;
    User(IUser user) {
        this.user = user;
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
        return user.isBot();
    }
}
