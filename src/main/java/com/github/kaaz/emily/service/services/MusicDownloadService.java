package com.github.kaaz.emily.service.services;

import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.service.AbstractService;
import com.github.kaaz.emily.util.Log;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Made by nija123098 on 3/28/2017.
 */
public class MusicDownloadService extends AbstractService {
    private static final List<Track> Q = new CopyOnWriteArrayList<>();
    private static final Map<Track, Set<Consumer<Track>>> CONSUMER_MAP = new ConcurrentHashMap<>();
    public MusicDownloadService() {
        super(1000);
    }
    public static boolean isDownloaded(Track track){
        return track.file().exists() && !Q.contains(track);
    }
    public static void queueDownload(Track track, Consumer<Track> consumer){
        if (isDownloaded(track)){
            consumer.accept(track);
        }else{
            CONSUMER_MAP.computeIfAbsent(track, t -> new ConcurrentHashSet<>()).add(consumer);
            Q.add(track);
        }
        if (Q.size() > 50){
            Log.log("Download q over-sized: " + Q.size());
        }
    }
    @Override
    public void run() {
        while (true){
            if (Q.size() < 1){
                return;
            }
            if (!isDownloaded(Q.get(0))){
                download(Q.get(0));
            }
            Track t = Q.remove(0);
            CONSUMER_MAP.remove(t).forEach(consumer -> consumer.accept(t));
        }
    }
    @Override
    public boolean mayBlock(){
        return true;
    }
    private static void download(Track track){// will block

    }
}
