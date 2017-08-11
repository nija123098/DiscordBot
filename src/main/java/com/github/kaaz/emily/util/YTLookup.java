package com.github.kaaz.emily.util;

import com.github.kaaz.emily.audio.YoutubeTrack;
import com.github.kaaz.emily.exeption.BotException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.launcher.BotConfig;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.SearchListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 8/9/2017.
 */
public class YTLookup {
    private static final YouTube YOUTUBE;
    private static final ConcurrentHashMap<String, List<YoutubeTrack>> CACHE = new ConcurrentHashMap<>();
    private static final YouTube.Search.List SINGLE;
    private static final YouTube.PlaylistItems.List PLAYLIST;
    static {
        YOUTUBE = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), (HttpRequest request) -> {}).setApplicationName("Emily").build();
        YouTube.Search.List list = null;
        try{list = YOUTUBE.search().list("id,snippet");
            list.setKey(BotConfig.GOOGLE_API_KEY);
            list.setOrder("relevance");
            list.setVideoCategoryId("10");
            list.setType("video");
            list.setFields("items(id/kind,id/videoId,snippet/title)");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SINGLE = list;
        YouTube.PlaylistItems.List playlist = null;
        playlist = errorWrap(() -> YOUTUBE.playlistItems().list("id,contentDetails,snippet"));
        playlist.setKey(BotConfig.GOOGLE_API_KEY);
        playlist.setFields("items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");
        PLAYLIST = playlist;
    }
    public static YoutubeTrack getTrack(String search){
        return getTrack(search, 1).get(0);
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
        return CACHE.get(search);
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
    private static <E> E errorWrap(GoogleResponseSupplier<E> supplier) {
        try{return supplier.get();
        } catch (IOException e) {
            if (e.getMessage().contains("quotaExceeded") || e.getMessage().contains("keyInvalid")) throw new BotException("Please use URLs to play music", e);
            throw new DevelopmentException(e);
        }
    }
    private interface GoogleResponseSupplier<E> {
        E get() throws IOException;
    }
}
