package com.github.kaaz.emily.util;

import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.launcher.BotConfig;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Helper class to search for tracks on youtube
 *
 * @author Kaaz
 */
public class YTSearch {
    private static final YouTube YOUTUBE;
    private static final YouTube.Search.List SEARCH;
    private static final ConcurrentHashMap<String, SimpleResult> CACHE = new ConcurrentHashMap<>();
    private static final Queue<String> KEY_QUEUE;
    private static volatile boolean hasValidKey = true;
    static {
        KEY_QUEUE = new LinkedList<>();
        Collections.addAll(KEY_QUEUE, BotConfig.GOOGLE_API_KEY);
        YOUTUBE = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), (HttpRequest request) -> {
        }).setApplicationName("Emily").build();
        YouTube.Search.List tmp;
        try {
            tmp = YOUTUBE.search().list("id,snippet");
            tmp.setOrder("relevance");
            tmp.setVideoCategoryId("10");
        } catch (IOException ex) {
            throw new DevelopmentException("Failed to initialize search", ex);
        }

        SEARCH = tmp;
        SEARCH.setType("video");
        SEARCH.setFields("items(id/kind,id/videoId,snippet/title)");
        setupNextKey();
    }

    public static boolean hasValidKey() {
        return hasValidKey;
    }

    public static synchronized void addYoutubeKey(String key) {
        KEY_QUEUE.add(key);
        hasValidKey = true;
    }

    private static synchronized boolean setupNextKey() {
        if (KEY_QUEUE.size() > 0) {
            String key = KEY_QUEUE.poll();
            if (key != null) {
                SEARCH.setKey(key);
                hasValidKey = true;
                return true;
            }
        }
        hasValidKey = false;
        return false;
    }

    public static SimpleResult getResults(String query) {
        String queryName = query.trim().toLowerCase();
        SimpleResult result = CACHE.get(queryName);
        if (result != null) {
            return result;
        }
        List<SimpleResult> results = getResults(query, 1);
        if (results != null && !results.isEmpty()) {
            CACHE.put(queryName, results.get(0));
            return results.get(0);
        }
        return null;
    }

    public static List<SimpleResult> getPlayListItems(String playlistCode) {
        List<SimpleResult> playlist = new ArrayList<>();
        try {
            YouTube.PlaylistItems.List playlistRequest = YOUTUBE.playlistItems().list("id,contentDetails,snippet");
            playlistRequest.setPlaylistId(playlistCode);
            playlistRequest.setKey(SEARCH.getKey());
            playlistRequest.setFields("items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");
            String nextToken = "";
            do {
                playlistRequest.setPageToken(nextToken);
                PlaylistItemListResponse playlistItemResult = playlistRequest.execute();
                playlist.addAll(playlistItemResult.getItems().stream().map(playlistItem -> new SimpleResult(playlistItem.getContentDetails().getVideoId(), playlistItem.getSnippet().getTitle())).collect(Collectors.toList()));
                nextToken = playlistItemResult.getNextPageToken();
            } while (nextToken != null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return playlist;
    }

    public static List<SimpleResult> getResults(String query, int numresults) {
        List<SimpleResult> urls = new ArrayList<>();
        SEARCH.setQ(query);
        SEARCH.setMaxResults((long) numresults);

        SearchListResponse searchResponse;
        try {
            searchResponse = SEARCH.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            searchResultList.forEach((sr) -> urls.add(new SimpleResult(sr.getId().getVideoId(), sr.getSnippet().getTitle())));
        } catch (GoogleJsonResponseException e) {
            if (e.getMessage().contains("quotaExceeded") || e.getMessage().contains("keyInvalid")) {
                if (setupNextKey()) {
                    return getResults(query, numresults);
                }
            } else {
                // Log.log("youtube-search-error <-- code :" + e.getDetails().getCode() + "message:" + e.getDetails().getMessage(), e);
            }
        } catch (IOException ex) {
            Log.log("YTSearch failure: " + ex.toString(), ex);
            return null;
        }
        return urls;
    }

    public static void resetCache() {
        CACHE.clear();
    }

    public static void throwLimitException(){
        throw new DevelopmentException("The youtube search limit has been hit for today, sorry.  Please use video urls.");
    }

    public static class SimpleResult {
        private final String code;
        private final String title;

        private SimpleResult(String code, String title) {
            this.code = code;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public String getCode() {
            return code;
        }
    }
}
