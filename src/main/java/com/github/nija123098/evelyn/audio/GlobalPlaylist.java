package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.audio.configs.track.BannedTrackConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.PermissionsException;
import com.github.nija123098.evelyn.favor.FavorHandler;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.util.Rand;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A special playlist which has no contents, it's
 * tracks are determined by popularity of all tracks.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class GlobalPlaylist extends Playlist {
    public static final GlobalPlaylist GLOBAL_PLAYLIST = new GlobalPlaylist();
    private GlobalPlaylist() {
        super(Playlist.GLOBAL_PLAYLIST_ID, true);
        Launcher.registerStartup(GlobalPlaylist::loadTracks);
    }
    @Override
    public String getID() {
        return Playlist.GLOBAL_PLAYLIST_ID;
    }
    @Override
    public void checkPermissionToEdit(User user, Guild guild) {
        throw new PermissionsException("You can't edit the global playlist silly");
    }
    @Override
    public String getName() {
        return "Global Playlist";
    }
    @Override
    public Configurable getOwner() {
        return DiscordClient.getOurUser();
    }
    @Override
    public Track getNext(Guild guild) {
        if (TRACKS.size() < 5) loadTracks();
        return TRACKS.isEmpty() ? null : Rand.getRand(TRACKS, true);
    }
    @Override
    public int getSize() {
        return size;
    }
    private static final List<Track> TRACKS = new CopyOnWriteArrayList<>();
    private static int size = Integer.MAX_VALUE;
    private static void loadTracks(){
        Map<Track, Double> map = new HashMap<>();
        List<Track> list = ConfigHandler.getTypeInstances(Track.class);
        list.stream().filter(track -> !ConfigHandler.getSetting(BannedTrackConfig.class, track)).forEach(track -> map.put(track, Math.log(Math.min(1, FavorHandler.getFavorAmount(track)))));
        AtomicDouble favor = new AtomicDouble();
        map.values().forEach(favor::addAndGet);
        favor.set(favor.get() / map.size() * 4);
        size = 0;
        TRACKS.clear();
        map.forEach((track, favorAmount) -> {
            if (favorAmount >= favor.get()) {
                TRACKS.add(track);
                ++size;
            }
        });
    }
}
