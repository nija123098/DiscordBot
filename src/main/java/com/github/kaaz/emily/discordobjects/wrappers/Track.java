package com.github.kaaz.emily.discordobjects.wrappers;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.configs.track.TrackTimeExpireConfig;
import com.github.kaaz.emily.config.configs.track.TrackFileConfig;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import com.github.kaaz.emily.service.services.MusicDownloadService;

import java.io.File;
import java.util.Map;

/**
 * Made by nija123098 on 3/28/2017.
 */
public class Track implements Configurable{
    private static final Map<String, Track> TRACK_MAP = new MemoryManagementService.ManagedMap<>(300000);// 5 min
    public static Track getTrack(String id){
        return TRACK_MAP.computeIfAbsent(id, s -> new Track(id));
    }
    public static Track getTrack(Platform platform, String id){
        return getTrack(platform.name() + "-" + id);
    }
    private String id;
    private Track(String id) {
        this.id = id.intern();
    }
    public boolean isDownloaded(){
        return MusicDownloadService.isDownloaded(this);
    }// no need to have a future since the File object will always exist
    @Override
    public String getID() {
        return this.id;
    }
    public Platform getPlatform(){
        return Platform.valueOf(this.id.split("-")[0]);
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.TRACK;
    }
    public File file(){// used when playing, and deleting, but at deletion it does not matter
        ConfigHandler.setSetting(TrackTimeExpireConfig.class, this, System.currentTimeMillis());
        return ConfigHandler.getSetting(TrackFileConfig.class, this);
    }
    @Override
    public boolean equals(Object o){
        return o instanceof Track && this.id.equals(((Track) o).id);
    }
    @Override
    public int hashCode(){
        return this.id.hashCode();
    }// id is interned
    public enum Platform{
        YOUTUBE,
        SOUNDCLOUD,;
    }
}
