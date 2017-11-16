package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.BotConfig.BotConfig;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.audio.YoutubeTrack;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.BotException;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
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
    private static final List<String> KEYS = new ArrayList<>();
    private static final AtomicInteger KEY_INDEX = new AtomicInteger(-1);
    private static boolean isYoutubeVideoCode(String s){
        return VALID_CODES.computeIfAbsent(s, s1 -> {
            if (s.length() != 11) return false;
            for (char c : s.toCharArray()) if (!(Character.isLetterOrDigit(c) || c == '-' || c == '_')) return false;
            return getTrackName(s) != null;
        });
    }
    private static String getTrackName(String code){
        try {
            String content = StringHelper.readAll("https://www.youtube.com/oembed?url=http%3A//youtube.com/watch%3Fv%3D" + code);
            return content.equals("Unauthorized") ? YTUtil.getVideoName(code) : ((JSONObject) new JSONParser().parse(content)).get("title").toString();
        } catch (IOException | ParseException | UnirestException e) {
            return null;
        }
    }
    public static String extractVideoCode(String s){
        int ind = s.indexOf('&');
        if (ind != -1) {
            if (s.startsWith("&list=", 43)) throw new ArgumentException("When you give me a link to a song and playlist I don't know what to play!");
            s = s.substring(0, ind);
        }
        if (s.length() == 11 && isYoutubeVideoCode(s)) return s;
        s = NetworkHelper.stripProtocol(s);
        if (FormatHelper.filtering(s, Character::isLetter).contains("youtube")){
            String cut;
            for (int i = 7; i < s.length() - 10; i++) {//7 for youtube, 10 for the code
                cut = s.substring(i, i + 11);
                if (isYoutubeVideoCode(cut)) return cut;
            }// backwards might be more efficient but parameters in links are being used more
        }
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
        YOUTUBE = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), (HttpRequest request) -> {}).setApplicationName("Evelyn").build();
        YouTube.Search.List list = YTUtil.errorWrap(() -> YTUtil.YOUTUBE.search().list("id,snippet"));
        list.setOrder("relevance");
        list.setVideoCategoryId("10");
        list.setType("video");
        list.setFields("items(id/kind,id/videoId,snippet/title)");
        SINGLE = list;
        YouTube.PlaylistItems.List playlist = YTUtil.errorWrap(() -> YTUtil.YOUTUBE.playlistItems().list("id,contentDetails,snippet"));
        playlist.setFields("items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");
        PLAYLIST = playlist;
        Collections.addAll(KEYS, BotConfig.GOOGLE_API_KEY.split(" "));
    }
    public static String getKey(){
        return KEYS.get(KEY_INDEX.incrementAndGet() % KEYS.size());
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
            list.setKey(getKey());
            list.setQ(search);
            list.setMaxResults((long) count);
            SearchListResponse searchResponse = errorWrap(list::execute);
            searchResponse.getItems().forEach((sr) -> {
                YoutubeTrack track = (YoutubeTrack) Track.getTrack(YoutubeTrack.class, sr.getId().getVideoId());
                track.setName(sr.getSnippet().getTitle());
                tracks.add(track);
            });
            CACHE.put(reduction, tracks);
        }
        return CACHE.get(reduction);
    }

    public static List<YoutubeTrack> getPlaylist(String code){
        YouTube.PlaylistItems.List list = (YouTube.PlaylistItems.List) PLAYLIST.clone();
        list.setKey(getKey());
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

    public static String getVideoName(String code){
        try {
            return YOUTUBE.videos().list("snippet").setKey(getKey()).set("hl", "en").setId(code).execute().getItems().get(0).getSnippet().getTitle();
        } catch (Exception e) {
            Log.log("Exception while getting video name from Youtube: " + code, e);
        }
        return null;
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
