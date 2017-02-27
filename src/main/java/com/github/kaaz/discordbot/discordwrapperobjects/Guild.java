package com.github.kaaz.discordbot.discordwrapperobjects;

import com.github.kaaz.discordbot.config.ConfigLevel;
import com.github.kaaz.discordbot.config.Configurable;
import sx.blah.discord.handle.obj.IGuild;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Guild implements Configurable {// todo rewrite to completely match necessary Discord stuff
    private static final Map<String, Guild> MAP = new ConcurrentHashMap<>();
    static Guild getGuild(IGuild guild){
        return MAP.computeIfAbsent(guild.getID(), s -> new Guild(guild));
    }
    public synchronized void update(IGuild guild){// hash is based on id, so no old guild is necessary
        MAP.get(guild.getID()).guild.set(guild);
    }
    final AtomicReference<IGuild> guild;
    private Guild(IGuild guild) {
        this.guild = new AtomicReference<>(guild);
    }
    @Override
    public String getID() {
        return null;
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.GUILD;
    }
    public User getOwner() {
        return new User(guild.get().getOwner());// temp
    }
    public EnumSet<DiscordPermission> getPermissionsForGuild(User user){
        return EnumSet.allOf(DiscordPermission.class);// temp
    }
}
