package com.github.kaaz.emily.discordobjects.wrappers;

import com.github.kaaz.emily.audio.configs.track.TrackDeleteTimeConfig;
import com.github.kaaz.emily.audio.configs.track.TrackFileConfig;
import com.github.kaaz.emily.audio.configs.track.TrackTimeExpireConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import com.github.kaaz.emily.service.services.MusicDownloadService;
import com.github.kaaz.emily.util.YTDLHelper;

import java.io.File;
import java.util.Map;
import java.util.function.Function;

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
    protected Track() {}
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
    @Override
    public String getName() {
        return this.getCode();// todo upgrade
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
        return new File(BotConfig.AUDIO_PATH + "\\" + this.id + "." + BotConfig.AUDIO_FORMAT);
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
    public void manage(){
        if (MusicDownloadService.isDownloaded(this) && ConfigHandler.getSetting(TrackTimeExpireConfig.class, this) > ConfigHandler.getSetting(TrackDeleteTimeConfig.class, GlobalConfigurable.GLOBAL) + System.currentTimeMillis()){
            ConfigHandler.getSetting(TrackFileConfig.class, this).delete();// is downloaded ensures that the file is not null
        }
    }
    @Override
    public void checkPermissionToEdit(User user, Guild guild) {
        BotRole.GUILD_TRUSTEE.checkRequiredRole(user, null);
    }
    public String getSource() {
        return this.getPlatform().getSource(this.getID());
    }
    public String getCode(){
        return this.id.substring(this.getPlatform().name().length() + 1);
    }
    public enum Platform{
        YOUTUBE(s -> "https://www.youtube.com/watch?v=" + s),
        SOUNDCLOUD(s -> "https://soundcloud.com/" + s.replace("-", "/")),;
        private final Function<String, String> TO_SOURCE;
        private final Function<Track, Boolean> DOWNLOAD;
        Platform(Function<String, String> toSource, Function<Track, Boolean> download) {
            this.TO_SOURCE = toSource;
            this.DOWNLOAD = download;
        }
        Platform(Function<String, String> toSource) {
            this(toSource, track -> YTDLHelper.download(track.getSource(), track.getID()));
        }
        public String getSource(String id){
            return TO_SOURCE.apply(id.substring(this.name().length() + 1));
        }
        public boolean download(Track track){
            return DOWNLOAD.apply(track);
        }
    }
}
