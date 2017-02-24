package com.github.kaaz.discordbot.discordwrapperobjects;

import com.github.kaaz.discordbot.config.ConfigLevel;
import com.github.kaaz.discordbot.config.Configurable;
import sx.blah.discord.handle.obj.IGuild;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class Guild implements Configurable {// todo rewrite
    private static final List<Guild> GUILDS = new ArrayList<>();
    public static synchronized Guild getGuild(IGuild guild){// haven't thought this out quite all the way through
        for (int i = 0; i < GUILDS.size(); i++) {
            if (GUILDS.get(i).guild.equals(guild)){
                return GUILDS.get(i);
            }
        }
        Guild g = new Guild(guild);
        GUILDS.add(g);
        return g;
    }
    IGuild guild;
    Guild(IGuild guild) {
        this.guild = guild;
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
        return new User(guild.getOwner());// temp
    }
    public EnumSet<DiscordPermission> getPermissionsForGuild(Guild guild){
        return EnumSet.allOf(DiscordPermission.class);// temp
    }
}
