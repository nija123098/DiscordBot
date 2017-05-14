package com.github.kaaz.emily.service.services;

import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.service.AbstractService;
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
    private static final Set<Track> DOWNLOADING = new ConcurrentHashSet<>();
    private static final List<List<Track>> Q = new CopyOnWriteArrayList<>();
    private static final Map<Track, Integer> PRIORITY_MAP = new ConcurrentHashMap<>();
    private static final Map<Track, Set<Consumer<Track>>> CONSUMER_MAP = new ConcurrentHashMap<>();
    private static Track getNext(){
        for (List<Track> aQ : Q) if (aQ.size() != 0) return aQ.get(0);
        return null;
    }
    public MusicDownloadService() {
        super(-1);
    }
    public static boolean isDownloaded(Track track){
        return track.file().exists() && !DOWNLOADING.contains(track);
    }
    public static void ensureDownload(int priority, Track track, Consumer<Track> consumer){
        if (isDownloaded(track)) {
            consumer.accept(track);
            return;
        }
        if (DOWNLOADING.contains(track)) {
            CONSUMER_MAP.put(track, new ConcurrentHashSet<>()).add(consumer);
            return;
        }
        Integer originalPriority = PRIORITY_MAP.get(track);
        if (originalPriority > priority) return; else PRIORITY_MAP.put(track, priority);
        while (true) if (Q.size() + 1 < priority) Q.add(new CopyOnWriteArrayList<>()); else break;
        Q.get(priority).add(track);
        CONSUMER_MAP.computeIfAbsent(track, t -> new ConcurrentHashSet<>()).add(consumer);
    }
    @Override
    public void run() {
        while (true){
            Track track = getNext();
            if (track == null) return;
            download(track);
            DOWNLOADING.remove(track);
            CONSUMER_MAP.remove(track).forEach(consumer -> consumer.accept(track));
        }
    }
    @Override
    public boolean mayBlock(){
        return true;
    }
    private static void download(Track track){// will block

    }
}
