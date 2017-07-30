package com.github.kaaz.emily.util;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.audio.YoutubeTrack;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class YTUtil {
    private static final String BASE_VIDEO_URL = "https://www.youtube.com/watch?v=";
    private static final String BASE_PLAYLIST_URL = "https://www.youtube.com/playlist?list=";
    private static boolean isYoutubeVideoCode(String s){
        return s.length() == 11 && NetworkHelper.isValid(BASE_VIDEO_URL + s);
    }
    public static String extractVideoCode(String s){
        int ind = s.indexOf('&');
        if (ind != -1) s = s.substring(0, ind);
        s = NetworkHelper.stripProtocol(s);
        if (s.startsWith("www.youtube.com/watch?v=")) s = s.substring(24);
        else if (s.startsWith("youtu.be/")) s = s.substring(9);
        else if (s.length() == 11 && isYoutubeVideoCode(s)) return s;
        else return null;
        if (isYoutubeVideoCode(s.substring(0, 11))) return s;
        return null;
    }
    private static boolean isYoutubePlaylistCode(String s){
        for (int i = 0; i < 34; i++) if (!Character.isLetterOrDigit(s.charAt(i)) || s.charAt(i) == '-' || s.charAt(i) == '_') return false;
        return NetworkHelper.isValid(BASE_PLAYLIST_URL + s);
    }
    public static String extractPlaylistCode(String s){
        int ind = s.indexOf('&');
        if (ind != -1) s = s.substring(0, ind);
        s = NetworkHelper.stripProtocol(s);
        if (s.startsWith("www.youtube.com/playlist?list=")) {
            s = s.substring(30);
        }
        if (s.length() == 34 && isYoutubePlaylistCode(s.substring(0, 30))) return s;
        return null;
    }
    public static List<Track> getTracksFromPlaylist(String code){
        return YTSearch.getPlayListItems(code).stream().map(YTSearch.SimpleResult::getCode).map(YoutubeTrack::new).collect(Collectors.toList());
    }
}
