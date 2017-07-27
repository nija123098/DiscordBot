package com.github.kaaz.emily.audio;

import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.launcher.Reference;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.*;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.reflections.Reflections;

import java.util.*;
import java.util.function.Function;

/**
 * Made by nija123098 on 3/28/2017.
 */
public abstract class Track implements Configurable{
    private static final Map<String, Class<? extends Track>> CLASS_MAP = new HashMap<>();
    private static final Map<Class<? extends Track>, Function<String, Track>> CODE_MAP = new HashMap<>();
    private static final Map<Class<? extends Track>, Function<String, Track>> ID_MAP = new HashMap<>();
    static {
        new Reflections(Reference.BASE_PACKAGE).getSubTypesOf(Track.class).forEach(clazz -> {
            try{Class.forName(clazz.getName());
            } catch (ClassNotFoundException e) {
                Log.log("Error while loading track type " + clazz.getName(), e);
            }
        });
    }
    static <T extends Track> void registerTrackType(Class<T> clazz, Function<String, Track> fromCode, Function<String, Track> fromID){
        CLASS_MAP.put(clazz.getSimpleName().toUpperCase(), clazz);
        CODE_MAP.put(clazz, fromCode);
        ID_MAP.put(clazz, fromID);
    }
    public static Track getTrack(String id){
        if (id == null) return null;
        String[] split = id.split("-");
        Class<?> clazz = CLASS_MAP.get(split[0].toUpperCase());
        if (clazz == null) return null;
        Function<String, Track> function = ID_MAP.get(clazz);
        return function == null ? null : function.apply(id);
    }
    public static Track getTrack(Class<? extends Track> clazz, String code){
        return CODE_MAP.get(clazz).apply(code);
    }
    public static List<Track> getTracks(String s){// may want to move
        Track track = getTrack(s);
        if (track != null) return Collections.singletonList(track);
        String code = YTUtil.extractVideoCode(s);
        if (code == null) {
            YTSearch.SimpleResult result = YTSearch.getResults(s);
            if (result != null) code = result.getCode();
        }
        if (code != null) {
            return Collections.singletonList(Track.getTrack(YoutubeTrack.class, code));
        }
        code = YTUtil.extractPlaylistCode(s);
        if (code != null){
            return YTUtil.getTracksFromPlaylist(s);
        }
        List<Track> tracks = SCUtil.extractTracks(s);
        if (tracks != null){
            return tracks;
        }
        code = TwitchUtil.extractCode(s);
        if (code != null){
            Collections.singletonList(Track.getTrack(TwitchTrack.class, code));
        }
        return new ArrayList<>(1);
    }
    private String id;
    protected Track() {}
    protected Track(String id) {
        String start = this.getClass().getSimpleName().toUpperCase();
        this.id = id.startsWith(start) ? id : start + "-" + id;
    }
    @Override
    public String getID() {
        return this.id;
    }
    final String getSpecificID(){
        return this.id.substring(this.id.split("-")[0].length() + 1);
    }
    public String getCode() {
        return this.getSpecificID();
    }
    @Override
    public String getName() {
        return this.getCode();// todo upgrade
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.TRACK;
    }
    @Override
    public boolean equals(Object o){
        return o instanceof Track && this.id.equals(((Track) o).id);
    }
    @Override
    public int hashCode(){
        return this.id.hashCode();
    }// id is interned
    @Override
    public void manage(){}
    @Override
    public void checkPermissionToEdit(User user, Guild guild) {
        BotRole.BOT_ADMIN.checkRequiredRole(user, null);
    }
    public abstract String getSource();
    public abstract String getPreviewURL();
    public abstract String getInfo();
    public abstract AudioTrack getTrack();
    public abstract Long getLength();
}
