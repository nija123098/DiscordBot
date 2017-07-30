package com.github.kaaz.emily.audio;

import com.github.kaaz.emily.audio.configs.track.BannedTrackConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.PermissionsException;
import com.github.kaaz.emily.favor.FavorHandler;
import com.github.kaaz.emily.util.Rand;
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
    private static AtomicInteger minimumTracks = new AtomicInteger();
    private GlobalPlaylist() {
        super(Playlist.GLOBAL_PLAYLIST_ID);
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
        return TRACKS.isEmpty() ? null : TRACKS.remove(Rand.getRand(TRACKS.size() - 1));
    }
    private static final List<Track> TRACKS = new CopyOnWriteArrayList<>();
    private static void loadTracks(){
        Map<Track, Float> map = new HashMap<>();
        List<Track> list = DownloadableTrack.getDownloadedTracks();
        list.forEach(System.out::println);
        list.stream().filter(track -> !ConfigHandler.getSetting(BannedTrackConfig.class, track)).forEach(track -> map.put(track, FavorHandler.getFavorAmount(track)));
        AtomicDouble favor = new AtomicDouble();
        map.values().forEach(favor::addAndGet);
        favor.set(favor.get() / map.size() * 2);
        TRACKS.clear();
        map.forEach((track, aFloat) -> {
            if (favor.get() > aFloat) TRACKS.add(track);
        });
    }
}
