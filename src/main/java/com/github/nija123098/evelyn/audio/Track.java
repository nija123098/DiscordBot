package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.config.ConfigLevel;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.SCUtil;
import com.github.nija123098.evelyn.util.TwitchUtil;
import com.github.nija123098.evelyn.util.YTUtil;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.reflections.Reflections;

import java.util.*;
import java.util.function.Function;

/**
 * A audio track for playing audio in a voice channel
 * by the bot.  Every implementation of this class has
 * a platform for which the track is intended to be
 * pulled from.  IE:  YoutubeTrack for Youtube videos.
 *
 * Tracks only specify audio data, no video data is used.
 *
 * @author nija123098
 * @since 1.0.0
 */
public abstract class Track implements Configurable {
    private static final Map<String, Class<? extends Track>> CLASS_MAP = new HashMap<>();
    private static final Map<Class<? extends Track>, Function<String, Track>> CODE_MAP = new HashMap<>();
    private static final Map<Class<? extends Track>, Function<String, Track>> ID_MAP = new HashMap<>();
    static {
        new Reflections(Launcher.BASE_PACKAGE).getSubTypesOf(Track.class).forEach(clazz -> {
            try{Class.forName(clazz.getName());
            } catch (ClassNotFoundException e) {
                Log.log("Error while loading track type " + clazz.getName(), e);
            }
        });
    }

    /**
     * Registers a class type for a track object.
     * Every track type implementation must cal this in it's initializer.
     *
     * @param clazz the type of the implementation.
     * @param fromCode the function to get the track from a given video or audio code.
     * @param fromID the function to get an instance from a given ID.
     * @param <T> the type for the track implementation.
     */
    static <T extends Track> void registerTrackType(Class<T> clazz, Function<String, Track> fromCode, Function<String, Track> fromID) {
        CLASS_MAP.put(clazz.getSimpleName().toUpperCase(), clazz);
        CODE_MAP.put(clazz, fromCode);
        ID_MAP.put(clazz, fromID);
    }

    /**
     * Gets the track with a given full ID.  Where the full ID is
     * the type name in caps dash a id based on the platform's code.
     * The id from the platform's code is to ensure that this formatting
     * for the id is not broken to not break other things.
     *
     * @param id the ID of the track.
     * @return the track instance for the given ID.
     */
    public static Track getTrack(String id) {
        if (id == null) return null;
        String[] split = id.split("-");
        Class<? extends Track> clazz = CLASS_MAP.get(split[0].toUpperCase());
        if (clazz == null) return null;
        Function<String, Track> function = ID_MAP.get(clazz);
        return function == null ? null : function.apply(id);
    }

    /**
     * Gets the track instance based on ID and platform dependent audio code.
     *
     * @param clazz the intended type of the track.
     * @param code the platform's code for the track.
     * @return the instance of the track specified by track type and code.
     */
    public static Track getTrack(Class<? extends Track> clazz, String code) {
        return CODE_MAP.get(clazz).apply(code);
    }

    /**
     * Given the string get a id or keywords to search Youtube.
     *
     * @param s the id or the keywords to search on Youtube.
     * @return the best related track instance for the given string.
     */
    public static List<Track> getTracks(String s) {// may want to move
        Track track = getTrack(s);
        if (track != null) return Collections.singletonList(track);
        String code = YTUtil.extractVideoCode(s);
        if (code != null) {
            return Collections.singletonList(Track.getTrack(YoutubeTrack.class, code));
        }
        code = YTUtil.extractPlaylistCode(s);
        if (code != null) {
            return YTUtil.getTracksFromPlaylist(s);
        }
        List<Track> tracks = SCUtil.extractTracks(s);
        if (tracks != null) {
            return tracks;
        }
        code = TwitchUtil.extractCode(s);
        //if (code != null) {
        //    return Collections.singletonList(Track.getTrack(TwitchTrack.class, code));
        //}
        Track t = YTUtil.getTrack(s);
        if (t != null) return Collections.singletonList(t);
        return new ArrayList<>(1);
    }
    private String id;
    protected Track() {}
    protected Track(String id) {
        String start = this.getClass().getSimpleName().toUpperCase();
        this.id = id.startsWith(start) ? id : start + "-" + id;
        //if (this.getLength() != null && this.getLength() > 10_800_000) throw new ArgumentException("I can't play songs that are over 3 hours in length, please use my repeat functionality.");
        if (!(this instanceof SpeechTrack)) this.registerExistence();
    }
    @Override
    public String getID() {
        return this.id;
    }

    /**
     * Gets the instance ID without the type prefix.
     *
     * @return the instance ID without the type prefix.
     */
    final String getSpecificID() {
        return this.id.substring(this.id.split("-")[0].length() + 1);
    }

    /**
     * Gets the platform specific ID.
     *
     * @return the platform specific ID.
     */
    public String getCode() {
        return this.getSpecificID();
    }
    @Override
    public String getName() {
        return this.getCode();
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.TRACK;
    }
    @Override
    public boolean equals(Object o) {
        return o instanceof Track && this.id.equals(((Track) o).id);
    }
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
    @Override
    public void checkPermissionToEdit(User user, Guild guild) {
        BotRole.BOT_ADMIN.checkRequiredRole(user, null);
    }

    /**
     * Gets the URL of the location the track source was gotten from.
     *
     * @return the URL of the location the track source was gotten from.
     */
    public abstract String getSource();
    public abstract String getPreviewURL();
    public abstract String getInfo();

    /**
     * Gets the audio track for Lavaplayer to process
     *
     * @param manager the manager that will play the {@link AudioTrack}.
     * @return the Lavaplayer {@link AudioTrack} to play.
     */
    public abstract AudioTrack getAudioTrack(GuildAudioManager manager);

    /**
     * Gets the length in millis or null if the instance is a stream.
     *
     * @return the length in millis or null if the instance is a stream.
     */
    public abstract Long getLength();

    /**
     * Returns if the track is able to be played at this time.
     *
     * @return if the track is able to be played at this time.
     */
    public boolean isAvailable() {
        return true;
    }
}
