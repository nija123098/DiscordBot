package com.github.kaaz.emily.audio;

import com.github.kaaz.emily.audio.configs.playlist.PlaylistContentsConfig;
import com.github.kaaz.emily.audio.configs.playlist.PlaylistPlayTypeConfig;
import com.github.kaaz.emily.command.annotations.LaymanName;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.exeption.PermissionsException;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.Rand;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * Made by nija123098 on 3/30/2017.
 */
@LaymanName(value = "Playlist", help = "The playlist type (global, guild, user) followed by the name")
public class Playlist implements Configurable {
    static final String GLOBAL_PLAYLIST_ID = "GLOBAL-PLAYLIST-ID";
    private static final Map<String, Playlist> MAP = new ConcurrentHashMap<>();
    static {
        ScheduleService.schedule(1000, () -> MAP.put(GLOBAL_PLAYLIST_ID, GlobalPlaylist.GLOBAL_PLAYLIST));
    }
    public static Playlist getPlaylist(User user, Guild guild, String args){
        String[] arg = args.split(" ");
        String a = arg[0].toLowerCase();
        if (a.equals("global")){
            return GlobalPlaylist.GLOBAL_PLAYLIST;
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
        if (id == null || !id.startsWith("pl-ID-")) return null;
        return MAP.computeIfAbsent(id, s -> new Playlist(id));
    }
    private String id;
    Playlist() {}
    protected Playlist(String id) {
        this.registerExistence();
        if (!id.endsWith("pl-ID-")) id = "pl-ID-" + id;
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
        if (this.id.startsWith("user-")){
            if (!this.getOwner().equals(user)){
                throw new PermissionsException("You don't own this playlist, " + ((User) this.getOwner()).getDisplayName(guild) + " does");
            }
            BotRole.BOT_ADMIN.checkRequiredRole(user, null);
        }else if (this.id.startsWith("guild-")){
            BotRole.GUILD_TRUSTEE.checkRequiredRole(user, guild);
        }else throw new DevelopmentException("Unknown playlist owner type");
    }

    @Override
    public Configurable getGoverningObject(){
        return getOwner();
    }

    public String getName(){
        String name = this.id.substring(this.id.indexOf(':') + 1);
        return name.length() == 0 ? (this.getOwner() instanceof User ? (getOwner().getName() + "'s list") : getOwner().getName() + "'s list") : name;
    }

    public boolean hasGivenName(){
        return !this.id.substring(this.id.indexOf(':') + 1).isEmpty();
    }

    public Configurable getOwner() {
        return this.id.startsWith("user-") ? User.getUser(this.id.split("-")[1]) : Guild.getGuild(this.id.split("-")[1]);
    }

    public Track getNext(Guild guild){
        return ConfigHandler.getSetting(PlaylistPlayTypeConfig.class, this).decide.apply(this, guild);
    }

    public int getSize(){
        return ConfigHandler.getSetting(PlaylistContentsConfig.class, this).size();
    }

    private static final Map<Playlist, Map<Guild, Integer>> SEQUENTIAL_MAP = new ConcurrentHashMap<>();
    public enum PlayType {
        RANDOM((playlist, guild) -> {
            List<Track> list = ConfigHandler.getSetting(PlaylistContentsConfig.class, playlist);
            switch (list.size()){
                case 0:
                    return null;
                case 1:
                    return list.get(0);
                default:
                    return list.get(Rand.getRand(list.size() - 1));
            }
        }), SEQUENTIAL((playlist, guild) -> {
            List<Track> list = ConfigHandler.getSetting(PlaylistContentsConfig.class, playlist);
            switch (list.size()){
                case 0:
                    return null;
                case 1:
                    return list.get(0);
                default:
                    return list.get(SEQUENTIAL_MAP.computeIfAbsent(playlist, p -> new ConcurrentHashMap<>()).compute(guild, (g, integer) -> integer == null ? 0 : integer + 1));
            }
        }),;
        private BiFunction<Playlist, Guild, Track> decide;
        PlayType(BiFunction<Playlist, Guild, Track> decide) {
            this.decide = decide;
        }
    }
}
