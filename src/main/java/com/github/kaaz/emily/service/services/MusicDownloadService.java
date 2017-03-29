package com.github.kaaz.emily.service.services;

import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.service.AbstractService;
import com.github.kaaz.emily.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Made by nija123098 on 3/28/2017.
 */
public class MusicDownloadService extends AbstractService {
    private static final List<Consumer<Track>> CONSUMERS = new CopyOnWriteArrayList<>();//Consumer<Track>
    private static final List<Track> TRACKS = new CopyOnWriteArrayList<>();//Consumer<Track>
    public MusicDownloadService() {
        super(1000);
    }
    public static boolean isDownloaded(Track track){
        return track.file().exists() && !TRACKS.contains(track);
    }
    public static void queueDownload(Track track, Consumer<Track> consumer){
        if (isDownloaded(track)){
            consumer.accept(track);
        }else{
            CONSUMERS.add(consumer);
            TRACKS.add(track);
        }
        if (TRACKS.size() > 50){
            Log.log("Download q over-sized: " + TRACKS.size());
        }
    }
    @Override
    public void run() {
        while (true){
            if (TRACKS.size() < 1){
                return;
            }
            if (!isDownloaded(TRACKS.get(0))){
                download(TRACKS.get(0));
            }
            CONSUMERS.remove(0).accept(TRACKS.remove(0));
        }
    }
    @Override
    public boolean mayBlock(){
        return true;
    }
    private static void download(Track track){// will block

    }
}
