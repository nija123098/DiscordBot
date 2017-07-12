package com.github.kaaz.emily.service.services;

import com.github.kaaz.emily.audio.DownloadableTrack;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.service.AbstractService;
import com.github.kaaz.emily.util.Care;
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
    private static final Consumer<DownloadableTrack> NOTHING = o -> {};
    private static final Set<DownloadableTrack> DOWNLOADING = new ConcurrentHashSet<>();
    private static final List<List<DownloadableTrack>> Q = new CopyOnWriteArrayList<>();
    private static final Map<DownloadableTrack, Integer> PRIORITY_MAP = new ConcurrentHashMap<>();
    private static final Map<DownloadableTrack, Set<Consumer<DownloadableTrack>>> CONSUMER_MAP = new ConcurrentHashMap<>();
    private static DownloadableTrack getNext(){
        for (int i = Q.size() - 1; i > -1; --i) if (!Q.get(i).isEmpty()) return Q.get(i).remove(0);
        return null;
    }
    public MusicDownloadService() {
        super(-1);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> DOWNLOADING.forEach(track -> track.file().delete())));
        Thread t;
        for (int i = 0; i < BotConfig.MUSIC_DOWNLOAD_THREAD_COUNT; i++) {
            t = new Thread(this);
            t.setDaemon(true);
            t.start();
        }
    }
    public static boolean isDownloaded(DownloadableTrack track){
        return track.file().exists() && !DOWNLOADING.contains(track);
    }
    public static void queueDownload(int priority, DownloadableTrack track, Consumer<DownloadableTrack> consumer){
        if (consumer == null) consumer = NOTHING;
        if (isDownloaded(track)) {
            consumer.accept(track);
            return;
        }
        if (DOWNLOADING.contains(track)) {
            CONSUMER_MAP.get(track).add(consumer);
            return;
        }
        CONSUMER_MAP.computeIfAbsent(track, t -> new ConcurrentHashSet<>()).add(consumer);
        Integer originalPriority = PRIORITY_MAP.get(track);
        if (originalPriority != null){
            if (originalPriority > priority) return;
            Q.get(originalPriority).remove(track);
        }
        PRIORITY_MAP.put(track, priority);
        ensureQCapacity(priority);
        Q.get(priority).add(track);
    }
    @Override
    public void run() {
        while (true){
            DownloadableTrack track = getNext();
            if (track == null) {
                Care.less(() -> Thread.sleep(10_000));
                continue;
            }
            DOWNLOADING.add(track);
            track.download();
            DOWNLOADING.remove(track);
            CONSUMER_MAP.remove(track).forEach(consumer -> consumer.accept(track));
        }
    }
    @Override
    public boolean mayBlock(){
        return true;
    }
    private static void ensureQCapacity(int size){
        while (size > Q.size() - 1) Q.add(new CopyOnWriteArrayList<>());
    }
}
