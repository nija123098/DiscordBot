package com.github.kaaz.emily.audio;

import com.github.kaaz.emily.command.anotations.LaymanName;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.audio.configs.playlist.PlaylistContentsConfig;
import com.github.kaaz.emily.audio.configs.playlist.PlaylistNowPlayingConfig;
import com.github.kaaz.emily.audio.configs.playlist.PlaylistPlayTypeConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.exeption.PermissionsException;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.Rand;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

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
        String id = null;
        if (arg.length < 2){
            throw new ArgumentException("Those play lists must have a name");
        }
        switch (a) {
            case "server":
            case "guild":
                if (guild == null) {
                    throw new RuntimeException("Attempted to use a guild playlist outside of a guild");
                }
                id = "guild-" + guild.getID() + "-id:" + arg[1].toUpperCase();
                break;
            case "my":
            case "mine":
            case "user":
                if (user == null) {
                    throw new RuntimeException("Not sure how you did that, but there is no user for this context");
                }
                id = "user-" + user.getID() + "-id:" + (arg.length == 1 ? "" : arg[1].toUpperCase());
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
    protected Playlist() {}
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

    @Override
    public void checkPermissionToEdit(User user, Guild guild){
        if (this == GLOBAL_PLAYLIST){// identity because there is a single instance of the guild playlist
            throw new PermissionsException("You can't edit the global playlist");
        }
        if (this.id.startsWith("user-")){
            if (!this.getOwner().equals(user)){
                throw new PermissionsException("You don't own this playlist, " + ((User) this.getOwner()).getDisplayName(guild) + " does");
            }
        }else if (this.id.startsWith("guild-")){
            BotRole.GUILD_TRUSTEE.checkRequiredRole(user, guild);
        }else throw new DevelopmentException("Unknown playlist owner type");
        BotRole.BOT_ADMIN.checkRequiredRole(user, null);
    }

    @Override
    public Configurable getGoverningObject(){
        return getOwner();
    }

    public String getName(){
        if (this.id.equals(GLOBAL_PLAYLIST_ID)){
            return "Global Playlist";
        }
        String name = this.id.substring(this.id.indexOf(':') + 1);
        return name.length() == 0 ? (this.getOwner() instanceof User ? (((User) getOwner()).getName() + "'s list") : ((Guild) getOwner()).getName() + "'s list") : name;
    }

    public boolean hasGivenName(){
        return this.id.substring(this.id.indexOf(':') + 1).length() != 0;
    }

    public Configurable getOwner() {
        return this.id.equals(GLOBAL_PLAYLIST_ID) ? null : this.id.startsWith("user-") ? User.getUser(this.id.split("-")[1]) : Guild.getGuild(this.id.split("-")[1]);
    }

    public Track getNext(){
        return ConfigHandler.getSetting(PlaylistPlayTypeConfig.class, this).decide.apply(this);
    }

    public enum PlayType {
        RANDOM(playlist -> {
            List<Track> list = ConfigHandler.getSetting(PlaylistContentsConfig.class, playlist);
            switch (list.size()){
                case 0:
                    return null;
                case 1:
                    return list.get(0);
                default:
                    Integer current = ConfigHandler.getSetting(PlaylistNowPlayingConfig.class, playlist);
                    return list.get(current == null ? Rand.getRand(list.size() - 1) : Rand.getRand(list.size() - 1, current));
            }
        }), SEQUENTIAL(playlist -> {
            List<Track> list = ConfigHandler.getSetting(PlaylistContentsConfig.class, playlist);
            switch (list.size()){
                case 0:
                    return null;
                case 1:
                    return list.get(0);
                default:
                    return list.get(ConfigHandler.getSetting(PlaylistNowPlayingConfig.class, playlist));
            }
        }),;
        private Function<Playlist, Track> decide;
        PlayType(Function<Playlist, Track> decide) {
            this.decide = decide;
        }
    }
}
