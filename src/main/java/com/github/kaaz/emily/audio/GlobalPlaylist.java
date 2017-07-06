package com.github.kaaz.emily.audio;

import com.github.kaaz.emily.audio.configs.track.BannedTrackConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.configs.FavorConfig;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.PermissionsException;
import com.github.kaaz.emily.util.Rand;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Made by nija123098 on 7/4/2017.
 */
public class GlobalPlaylist extends Playlist {
    GlobalPlaylist() {}
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
    public Track getNext() {
        if (TRACKS.size() < 50) loadTracks();
        return TRACKS.remove(Rand.getRand(TRACKS.size() - 1));
    }
    private static final List<Track> TRACKS = new CopyOnWriteArrayList<>();
    private static final FavorConfig FAVOR_CONFIG = ConfigHandler.getConfig(FavorConfig.class);
    private static final BannedTrackConfig BAN_CONFIG = ConfigHandler.getConfig(BannedTrackConfig.class);
    private static void loadTracks(){
        Map<Track, Float> map = new HashMap<>();
        DownloadableTrack.getDownloadedTracks().stream().filter(track -> !BAN_CONFIG.getValue(track)).forEach(track -> map.put(track, FAVOR_CONFIG.getValue(track)));
        AtomicDouble favor = new AtomicDouble();
        map.values().forEach(favor::addAndGet);
        favor.set(favor.get() / map.size() * 2);
        map.forEach((track, aFloat) -> {
            if (favor.get() > aFloat) TRACKS.add(track);
        });
    }
}
