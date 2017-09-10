package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.audio.configs.track.BannedTrackConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.PermissionsException;
import com.github.nija123098.evelyn.favor.FavorHandler;
import com.github.nija123098.evelyn.util.Rand;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by nija123098 on 7/4/2017.
 */
public class GlobalPlaylist extends Playlist {
    public static final GlobalPlaylist GLOBAL_PLAYLIST = new GlobalPlaylist();
    private GlobalPlaylist() {
        super(Playlist.GLOBAL_PLAYLIST_ID, true);
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
    public boolean hasGivenName() {
        return false;
    }
    @Override
    public Configurable getOwner() {
        return DiscordClient.getOurUser();
    }
    @Override
    public Track getNext(Guild guild) {
        if (TRACKS.size() < 50) loadTracks();
        return TRACKS.isEmpty() ? null : Rand.getRand(TRACKS, true);
    }
    @Override
    public int getSize() {
        return size;
    }
    private static final List<Track> TRACKS = new CopyOnWriteArrayList<>();
    private static int size = Integer.MAX_VALUE;
    private static void loadTracks(){
        Map<Track, Float> map = new HashMap<>();
        List<Track> list = DownloadableTrack.getDownloadedTracks();
        list.stream().filter(track -> !ConfigHandler.getSetting(BannedTrackConfig.class, track)).forEach(track -> map.put(track, FavorHandler.getFavorAmount(track)));
        AtomicDouble favor = new AtomicDouble();
        map.values().forEach(favor::addAndGet);
        favor.set(favor.get() / map.size() * 2);
        size = 0;
        TRACKS.clear();
        map.forEach((track, aFloat) -> {
            if (aFloat >= favor.get()) {
                TRACKS.add(track);
                ++size;
            }
        });
    }
}
