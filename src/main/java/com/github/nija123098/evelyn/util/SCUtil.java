package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.audio.SoundCloudTrack;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Made by nija123098 on 6/7/2017.
 */
public class SCUtil {
    public static String extractCode(String s){
        s = NetworkHelper.stripProtocol(s);
        if (s.startsWith("soundcloud.com/")){
            s = s.substring(15).split(Pattern.quote("?"))[0];
        }
        return !s.isEmpty() && s.contains("/") && NetworkHelper.isValid("https://soundcloud.com/" + s) ? s : null;
    }
    public static Track extractVideoCode(String code){
        code = extractCode(code);
        if (code != null && code.contains("/sets/")) return null;
        return new SoundCloudTrack(code);
    }
    public static List<Track> extractPlaylistCode(String code){
        code = extractCode(code);
        if (code != null && !code.contains("/sets/")) return null;
        return getTracksFromList(code);
    }
    public static List<Track> extractTracks(String s){
        int ind = s.indexOf('&');
        if (ind != -1) s = s.substring(0, ind);
        s = extractCode(s);
        if (s == null) return null;
        return s.contains("/sets/") ? getTracksFromList(s) : Collections.singletonList(new SoundCloudTrack(s));
    }
    public static List<Track> getTracksFromList(String code){
        List<String> commands = new ArrayList<>();
        commands.add(ConfigProvider.librariesFiles.youtube_dl());
        commands.add("--flat-playlist");
        commands.add("--dump-json");
        commands.add("https://soundcloud.com/" + code);
        List<Track> tracks = new ArrayList<>();
        Process process = null;
        try {
            process = new ProcessBuilder(commands).start();
            InputStream stream = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {// 8 for util, 22 for http://sc.com
                line = line.substring(line.indexOf("\"url\": \"") + 30);
                tracks.add(new SoundCloudTrack(line.substring(0, line.indexOf("\""))));
            }
        } catch (IOException e) {
            Log.log("IOException loading tracks from Soundcloud", e);
            tracks = null;
        } finally {
            if (process != null) process.destroyForcibly();
        }
        return tracks;
    }
}
