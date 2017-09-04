package com.github.kaaz.emily.util;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.audio.YoutubeTrack;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.BotException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.launcher.BotConfig;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class YTUtil {
    private static final YouTube YOUTUBE;
    private static final YouTube.Search.List SINGLE;
    private static final YouTube.PlaylistItems.List PLAYLIST;
    private static final String BASE_VIDEO_URL = "https://www.youtube.com/watch?v=";
    private static final String BASE_PLAYLIST_URL = "https://www.youtube.com/playlist?list=";
    private static final ConcurrentHashMap<String, List<YoutubeTrack>> CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> VALID_CODES = new ConcurrentHashMap<>();
    private static boolean isYoutubeVideoCode(String s){
        return VALID_CODES.computeIfAbsent(s, s1 -> {
            if (s.length() != 11) return false;
            for (char c : s.toCharArray()) if (!(Character.isLetterOrDigit(c) || c == '-' || c == '_')) return false;
            try{return !StringHelper.readAll("https://www.youtube.com/oembed?url=http%3A//youtube.com/watch%3Fv%3D" + s).equals("Not Found");
            } catch (IOException | UnirestException e) {
                Log.log("Issue finding validity of youtube track: " + s);
                return false;
            }
        });
    }
    public static String extractVideoCode(String s){
        int ind = s.indexOf('&');
        if (ind != -1) {
            if (s.startsWith("&list=", 43)) throw new ArgumentException("When you give me a link to a song and playlist I don't know what to play!");
            s = s.substring(0, ind);
        }
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

    static {
        YOUTUBE = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), (HttpRequest request) -> {}).setApplicationName("Emily").build();
        YouTube.Search.List list = YTUtil.errorWrap(() -> YTUtil.YOUTUBE.search().list("id,snippet"));
        list.setKey(BotConfig.GOOGLE_API_KEY);
        list.setOrder("relevance");
        list.setVideoCategoryId("10");
        list.setType("video");
        list.setFields("items(id/kind,id/videoId,snippet/title)");
        SINGLE = list;
        YouTube.PlaylistItems.List playlist = YTUtil.errorWrap(() -> YTUtil.YOUTUBE.playlistItems().list("id,contentDetails,snippet"));
        playlist.setKey(BotConfig.GOOGLE_API_KEY);
        playlist.setFields("items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");
        PLAYLIST = playlist;
    }
    public static List<Track> getTracksFromPlaylist(String code){
        return new ArrayList<>(getPlaylist(code));
    }

    public static YoutubeTrack getTrack(String search){
        List<YoutubeTrack> tracks = getTrack(search, 1);
        return tracks.isEmpty() ? null : tracks.get(0);
    }

    public static List<YoutubeTrack> getTrack(String search, int count){
        String reduction = FormatHelper.filtering(search, Character::isLetterOrDigit).toLowerCase();
        if (!(CACHE.containsKey(reduction) && count <= CACHE.get(reduction).size())) {
            List<YoutubeTrack> tracks = new ArrayList<>(count);
            YouTube.Search.List list = (YouTube.Search.List) SINGLE.clone();
            list.setQ(search);
            list.setMaxResults((long) count);
            SearchListResponse searchResponse = errorWrap(list::execute);
            searchResponse.getItems().forEach((sr) -> tracks.add(new YoutubeTrack(sr.getId().getVideoId())));
            CACHE.put(reduction, tracks);
        }
        return CACHE.get(reduction);
    }

    public static List<YoutubeTrack> getPlaylist(String code){
        YouTube.PlaylistItems.List list = (YouTube.PlaylistItems.List) PLAYLIST.clone();
        List<YoutubeTrack> tracks = new ArrayList<>();
        String nextToken = "";
        do {
            list.setPageToken(nextToken);
            list.setPlaylistId(code);
            try{PlaylistItemListResponse playlistItemResult = list.execute();
                tracks.addAll(playlistItemResult.getItems().stream().map(playlistItem -> new YoutubeTrack(playlistItem.getContentDetails().getVideoId())).collect(Collectors.toList()));
                nextToken = playlistItemResult.getNextPageToken();
            } catch (IOException e) {throw new DevelopmentException("IO issue getting playlist contents", e);}
        } while (nextToken != null);
        return tracks;
    }

    static <E> E errorWrap(GoogleResponseSupplier<E> supplier) {
        try{return supplier.get();
        } catch (IOException e) {
            if (e.getMessage().contains("quotaExceeded") || e.getMessage().contains("keyInvalid")) throw new BotException("Please use URLs to play music", e);
            throw new YoutubeSearchException(e);
        }
    }

    private interface GoogleResponseSupplier<E> {
        E get() throws IOException;
    }

    public static class YoutubeSearchException extends BotException {
        private YoutubeSearchException(Throwable cause) {
            super(cause);
        }
    }
}
