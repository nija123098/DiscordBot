package com.github.kaaz.emily.audio;

import com.github.kaaz.emily.audio.configs.UserPlaylistsConfig;
import com.github.kaaz.emily.audio.configs.playlist.PlaylistContentsConfig;
import com.github.kaaz.emily.audio.configs.playlist.PlaylistPlayTypeConfig;
import com.github.kaaz.emily.command.annotations.LaymanName;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.PermissionsException;
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
    public static Playlist getPlaylist(User user, String name){
        return ConfigHandler.getSetting(UserPlaylistsConfig.class, user).contains(name.toLowerCase()) ? MAP.computeIfAbsent("pl-" + user.getID() + "-" + name.toLowerCase(), Playlist::new) : null;
    }
    public static Playlist getPlaylist(String id){
        if (id == null) return null;
        return MAP.computeIfAbsent(id, s -> new Playlist(id));
    }
    private String id;
    Playlist() {}
    protected Playlist(String id) {
        this.id = id;
        this.registerExistence();
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
        if (!this.getOwner().equals(user)){
            throw new PermissionsException("You don't own this playlist, " + ((User) this.getOwner()).getDisplayName(guild) + " does");
        }
    }

    @Override
    public Configurable getGoverningObject(){
        return getOwner();
    }

    public String getName(){
        return this.id.split("-")[2];
    }

    public boolean hasGivenName(){
        return !this.id.substring(this.id.indexOf(':') + 1).isEmpty();
    }

    public Configurable getOwner() {
        return User.getUser(this.id.split("-")[1]);
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
