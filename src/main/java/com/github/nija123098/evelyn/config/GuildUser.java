package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.exeption.ConfigurableConvertException;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The configurable for users within a guild
 */
public class GuildUser implements Configurable {
    private static final Map<Guild, Integer> NEXT_USER_INTEGER = new ConcurrentHashMap<>();
    static {
        EventDistributor.register(GuildUser.class);
        Launcher.registerStartup(() -> DiscordClient.getGuilds().forEach(GuildUser::orderGuildUsers));
    }

    @EventListener
    public static void handle(DiscordUserJoin join){
        orderGuildUsers(join.getGuild());
        NEXT_USER_INTEGER.compute(join.getGuild(), (guild, integer) -> getGuildUser(join.getGuild(), join.getUser()).number = ++integer);
    }

    @EventListener
    public static void handle(DiscordGuildJoin join){
        orderGuildUsers(join.getGuild());
    }

    /**
     * The map containing guild user configurables
     */
    private static final Map<String, GuildUser> GUILD_USERS = new HashMap<>();

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

    public static void orderGuildUsers(Guild guild){
        Map<Long, GuildUser> map = new ConcurrentHashMap<>();
        guild.getUsers().forEach(user -> map.put(guild.getJoinTimeForUser(user), getGuildUser(guild, user)));
        Long[] longs = map.keySet().toArray(new Long[map.keySet().size()]);
        Arrays.sort(longs);
        for (int i = 0; i < longs.length; i++) map.get(longs[i]).number = i;
        NEXT_USER_INTEGER.put(guild, longs.length - 1);
    }

    private transient int number = -1;
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
    public int getJoinPosition() {
        return this.number;
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
    public boolean shouldCache() {
        return false;
    }
    @Override
    public Configurable getGoverningObject(){
        return getGuild();
    }
    @Override
    public <T extends Configurable> Configurable convert(Class<T> t) {
        if (t.equals(GuildUser.class)) return this;
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
