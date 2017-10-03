package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.audio.configs.guild.GuildPlaylistsConfig;
import com.github.nija123098.evelyn.audio.configs.playlist.UserPlaylistsConfig;
import com.github.nija123098.evelyn.audio.configs.playlist.PlaylistContentsConfig;
import com.github.nija123098.evelyn.audio.configs.playlist.PlaylistPlayTypeConfig;
import com.github.nija123098.evelyn.command.annotations.LaymanName;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.ConfigLevel;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.exeption.PermissionsException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.Rand;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * A list of tracks for either guilds or users
 * which can be played in random or sequential
 * order and has a name and id.
 * pl-u/g-u/g id-name
 *
 * @author nija123098
 * @since 1.0.0
 * @see PlaylistContentsConfig
 */
@LaymanName(value = "Playlist", help = "The playlist type (global, guild, user) followed by the name")
public class Playlist implements Configurable {
    static final String GLOBAL_PLAYLIST_ID = "GLOBAL-PLAYLIST-ID";
    private static final Map<String, Playlist> MAP = new ConcurrentHashMap<>();
    static {
        ScheduleService.schedule(1000, () -> MAP.put(GLOBAL_PLAYLIST_ID, GlobalPlaylist.GLOBAL_PLAYLIST));
    }
    public static Playlist getPlaylist(User user, String name){
        if (name.isEmpty()) throw new ArgumentException("Your playlist must have a name");
        String reduced = FormatHelper.filtering(name, Character::isLetter);
        if (!name.equals(reduced)) throw new ArgumentException("A playlist name must only contain letters");
        return ConfigHandler.getSetting(UserPlaylistsConfig.class, user).contains(name.toLowerCase()) ? MAP.computeIfAbsent("pl-u-" + user.getID() + "-" + name.toLowerCase(), Playlist::new) : null;
    }
    public static Playlist getPlaylist(Guild guild, String name){
        if (name.isEmpty()) throw new ArgumentException("Your playlist must have a name");
        if (!name.equals(FormatHelper.filtering(name, Character::isLetter))) throw new ArgumentException("A playlist name must only contain letters");
        return ConfigHandler.getSetting(GuildPlaylistsConfig.class, guild).contains(name.toLowerCase()) ? MAP.computeIfAbsent("pl-g-" + guild.getID() + "-" + name.toLowerCase(), Playlist::new) : null;
    }
    public static Playlist getPlaylist(String id){
        if (id == null || !id.startsWith("pl-")) return null;
        String[] split = id.split("-");
        if (split.length != 4) return null;
        if (split[1].equals("u")) return getPlaylist(User.getUser(split[2]), split[3]);
        else return getPlaylist(Guild.getGuild(split[2]), split[3]);
    }
    private String id;
    Playlist() {}
    protected Playlist(String id) {
        this.id = id;
        this.registerExistence();
    }
    Playlist(String id, boolean globalPlaylist){
        this.id = id;
        if (!globalPlaylist) throw new DevelopmentException("WTF ARE YOU DOING?");
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
        if (this.id.startsWith("pl-u-")){
            if (!this.getOwner().equals(user)){
                throw new PermissionsException("You don't own this playlist, " + ((User) this.getOwner()).getDisplayName(guild) + " does");
            }
        }else{
            if (!BotRole.GUILD_TRUSTEE.hasRequiredRole(user, guild)) throw new PermissionsException("You must at least be a trustee edit a server playlist");
        }
    }

    @Override
    public Configurable getGoverningObject(){
        return getOwner();
    }

    public String getName(){
        return this.id.split("-")[3];
    }

    public Configurable getOwner() {// might want to use governing object instead
        return this.id.startsWith("pl-u-") ? User.getUser(this.id.split("-")[2]) : Guild.getGuild(this.id.split("-")[2]);
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
