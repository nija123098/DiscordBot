package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ConfigurableConvertException;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashMap;
import java.util.Map;

/**
 * The configurable for users within a guild
 */
public class GuildUser implements Configurable {
    /**
     * The map containing guild user configurables
     */
    public static final Map<String, GuildUser> GUILD_USERS = new HashMap<>();

    /**
     * The getter for a object that represents a guild user
     *
     * @param id the id of the guild user
     * @return the guild user object for the guild and user
     */
    public static GuildUser getGuildUser(String id){
        if (id.startsWith("gu-")){
            String[] split = id.substring(3).split("-id-");
            Guild guild = Guild.getGuild(split[0]);
            User user = User.getUser(split[1]);
            if (guild == null || user == null) return null;
            return GUILD_USERS.computeIfAbsent(id, s -> new GuildUser(id));
        }
        return null;
    }

    /**
     * The getter for a object that represents a guild user
     *
     * @param guild the guild for a guild user object
     * @param user the user for the guild object
     * @return the guild user object for the guild and user
     */
    public static GuildUser getGuildUser(Guild guild, User user){
        return getGuildUser("gu-" + guild.getID() + "-id-" + user.getID());
    }
    private String id;
    protected GuildUser() {}
    private GuildUser(String id) {
        this.id = id;
        if (this.getGuild() == null || this.getUser() == null) throw new DevelopmentException("Either the guild or user is null");
        this.registerExistence();
    }
    @Override
    public String getID() {
        return this.id;
    }
    @Override
    public String getName() {
        return getUser().getDisplayName(getGuild());
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.GUILD_USER;
    }
    public void checkPermissionToEdit(User user, Guild guild){
        if (user.equals(this.getUser())){
            return;
        }
        BotRole.GUILD_TRUSTEE.checkRequiredRole(user, guild);
    }
    @Override
    public boolean shouldCashe() {
        return false;
    }
    @Override
    public Configurable getGoverningObject(){
        return getGuild();
    }
    @Override
    public <T extends Configurable> Configurable convert(Class<T> t) {
        if (t.equals(Guild.class)) return this.getGuild();
        if (t.equals(User.class)) return this.getUser();
        throw new ConfigurableConvertException(this.getClass(), t);
    }
    @Override
    public boolean equals(Object o){
        return Configurable.class.isInstance(o) && this.getID().equals(((Configurable) o).getID());
    }
    public Guild getGuild(){
        String s = this.id.split("-id-")[0];
        return Guild.getGuild(s.substring(3, s.length()));
    }
    public User getUser(){
        return User.getUser(this.id.split("-id-")[1]);
    }
}
