package com.github.nija123098.evelyn.moderation.messagefiltering;

import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.util.CacheHelper;
import com.google.common.cache.LoadingCache;
import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;

import java.util.List;

public class URLScanning {
    private static final LoadingCache<Message, List<Url>> MESSAGE_CACHE = CacheHelper.getLoadingCache(Runtime.getRuntime().availableProcessors() * 2, 500, 5_000, message -> new UrlDetector(message.getContent(), UrlDetectorOptions.Default).detect());
    public static List<Url> getURLs(Message message) {
        return MESSAGE_CACHE.getUnchecked(message);
    }
}
