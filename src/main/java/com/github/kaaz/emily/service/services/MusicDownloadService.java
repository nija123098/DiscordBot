package com.github.kaaz.emily.service.services;

import com.github.kaaz.emily.audio.configs.track.DurrationTimeConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.service.AbstractService;
import com.github.kaaz.emily.util.Care;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.json.JSONObject;
import sun.misc.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
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
    public static boolean isDownloaded(Track track){
        return track.file().exists() && !DOWNLOADING.contains(track);
    }
    public static void ensureDownload(int priority, Track track, Consumer<Track> consumer){
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
            Track track = getNext();
            if (track == null) {
                Care.less(() -> Thread.sleep(1000));
                continue;
            }
            DOWNLOADING.add(track);
            track.getPlatform().download(track);// a download function in track may be misleading
            ConfigHandler.setSetting(DurrationTimeConfig.class, track, time(track.file()));
            DOWNLOADING.remove(track);
            CONSUMER_MAP.remove(track).forEach(consumer -> consumer.accept(track));
        }
    }
    @Override
    public boolean mayBlock(){
        return true;
    }
    private static Long time(File file) {
        Process ffprobeProcess = null;
        try {
            ffprobeProcess = new ProcessBuilder().command(Arrays.asList(
                    "ffprobe",
                    "-show_format",
                    "-print_format", "json",
                    "-loglevel", "0",
                    "-i", file.getPath()
            )).start();
            InputStream ffprobeStream = ffprobeProcess.getInputStream();
            byte[] infoData = IOUtils.readFully(ffprobeStream, -1, false);
            ffprobeProcess.waitFor(30, TimeUnit.SECONDS);
            if (infoData != null && infoData.length > 0) {
                System.out.println(new String(infoData));
                JSONObject json = new JSONObject(new String(infoData)).getJSONObject("format");
                int duration = (int) json.optDouble("duration", 0);
                if (duration != 0) {
                    ffprobeProcess.destroyForcibly();
                    return (long) duration;
                }
            }
        } catch (IOException | InterruptedException ignored) {
        } finally {
            if (ffprobeProcess != null) {
                ffprobeProcess.destroyForcibly();
            }
        }
        return null;
    }

    private static void ensureQCapacity(int size){
        while (size > Q.size() - 1) Q.add(new CopyOnWriteArrayList<>());
    }
}
