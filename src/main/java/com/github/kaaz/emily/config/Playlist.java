package com.github.kaaz.emily.config;

import com.github.kaaz.emily.command.anotations.LaymanName;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.exeption.PermissionsException;
import com.github.kaaz.emily.perms.BotRole;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 3/30/2017.
 */
@LaymanName(value = "Playlist", help = "The playlist type (global, guild, user) followed by the name")
public class Playlist implements Configurable {
    public static final String GLOBAL_PLAYLIST_ID = "GLOBAL-PLAYLIST-ID";
    public static final Playlist GLOBAL_PLAYLIST = new Playlist(GLOBAL_PLAYLIST_ID);
    private static final Map<String, Playlist> MAP = new ConcurrentHashMap<>();

    public static Playlist getPlaylist(User user, Guild guild, String args){
        String[] arg = args.split(" ");
        String a = arg[0].toLowerCase();
        if (a.equals("global")){
            return Playlist.MAP.computeIfAbsent(GLOBAL_PLAYLIST_ID, s -> new Playlist(GLOBAL_PLAYLIST_ID));
        }
        if (arg.length < 2){
            throw new RuntimeException("No specified playlist name");
        }
        String id = null;
        StringBuilder builder = new StringBuilder();
        switch (a) {
            case "guild":
                if (guild == null) {
                    throw new RuntimeException("Attempted to use a guild playlist outside of a guild");
                }
                id = "guild-" + guild.getID() + "-id:" + arg[1].toUpperCase();
                break;
            case "my":
            case "user":
                if (user == null) {
                    throw new RuntimeException("Not sure how you did that, but there is no user for this context");
                }
                id = "user-" + user.getID() + "-id:" + arg[1].toUpperCase();
                break;
        }
        if (id == null){
            throw new ArgumentException("No valid playlist argument, put `my` or `guild` in front of a playlist name or use `global`");
        }
        return MAP.computeIfAbsent(id, Playlist::new);
    }
    public static Playlist getPlaylist(String id){
        return MAP.computeIfAbsent(id, s -> new Playlist(id));
    }
    private String id;
    private Playlist(String id) {
        this.id = id;
    }
    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.PLAYLIST;
    }

    public void checkPermissionToEdit(User user, Guild guild){
        if (this == GLOBAL_PLAYLIST){// identity because there is a single instance of the guild playlist
            throw new PermissionsException("You can't edit the global playlist");
        }
        if (this.id.startsWith("user-")){
            if (!this.getOwner().equals(user)){
                throw new PermissionsException("You don't own this playlist, " + ((User) this.getOwner()).getDisplayName(guild) + " does");
            }
        }else if (this.id.startsWith("guild-")){
            BotRole.checkRequiredRole(BotRole.GUILD_TRUSTEE, user, guild);
        }else throw new DevelopmentException("Unknown playlist owner type");
        BotRole.checkRequiredRole(BotRole.BOT_ADMIN, user, null);
    }

    public String getName(){
        if (this.id.equals(GLOBAL_PLAYLIST_ID)){
            return "Global Playlist";
        }
        return this.id.substring(this.id.charAt(':'));
    }

    public Configurable getOwner() {
        return this.id.equals(GLOBAL_PLAYLIST_ID) ? null : this.id.startsWith("user-") ? User.getUser(this.id.split("-")[1]) : Guild.getGuild(this.id.split("-")[1]);
    }
}
