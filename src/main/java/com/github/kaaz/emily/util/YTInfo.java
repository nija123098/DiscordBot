package com.github.kaaz.emily.util;

import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.launcher.BotConfig;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 7/13/2017.
 */
public class YTInfo {
    private static final YouTube YOUTUBE;
    private static final YouTube.Search.List SEARCH;
    private static final ConcurrentHashMap<String, String> CACHE = new ConcurrentHashMap<>();
    private static volatile boolean hasValidKey = true;
    static {
        YOUTUBE = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), (HttpRequest request) -> {
        }).setApplicationName("Emily").build();
        YouTube.Search.List tmp;
        try {
            tmp = YOUTUBE.search().list("id,snippet,statistics,contentDetails");
            tmp.setOrder("relevance");
            tmp.setVideoCategoryId("10");
        } catch (IOException ex) {
            throw new DevelopmentException("Failed to initialize search", ex);
        }
        SEARCH = tmp;
        SEARCH.setType("video");
    }

    public static String getResults(String query) {
        String queryName = query.trim().toLowerCase();
        String result = CACHE.get(queryName);
        if (result != null) {
            return result;
        }
        List<String> results = getResults(query, 1);
        if (results != null && !results.isEmpty()) {
            CACHE.put(queryName, results.get(0));
            return results.get(0);
        }
        return null;
    }

    public static List<String> getResults(String query, long resultCount) {
        List<String> urls = new ArrayList<>();
        SEARCH.setQ(query);
        SEARCH.setMaxResults(resultCount);
        SearchListResponse searchResponse;
        try {
            searchResponse = SEARCH.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            searchResultList.forEach((sr) -> urls.add(sr.getId().getVideoId()));
        } catch (GoogleJsonResponseException e) {
            if (e.getMessage().contains("quotaExceeded") || e.getMessage().contains("keyInvalid")) Log.log("YouTube quota reached");
            else Log.log("youtube-search-error <-- code :" + e.getDetails().getCode() + "message:" + e.getDetails().getMessage(), e);
        } catch (IOException ex) {
            Log.log("YTSearch failure: " + ex.toString(), ex);
            return null;
        }
        return urls;
    }

    public static List<String> getPlaylistItems(String playlistCode){
        List<String> playlist = new ArrayList<>();
        try {
            YouTube.PlaylistItems.List playlistRequest = YOUTUBE.playlistItems().list("id");
            playlistRequest.setPlaylistId(playlistCode);
            playlistRequest.setKey(BotConfig.GOOGLE_API_KEY);
            playlistRequest.setFields("items(videoId),nextPageToken,pageInfo");
            String nextToken = "";
            do {
                playlistRequest.setPageToken(nextToken);
                PlaylistItemListResponse playlistItemResult = playlistRequest.execute();
                playlist.addAll(playlistItemResult.getItems().stream().map(PlaylistItem::getId).collect(Collectors.toList()));
                nextToken = playlistItemResult.getNextPageToken();
            } while (nextToken != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playlist;
    }
    public static void setYoutubeInfo(String info){

    }
}
