package com.github.nija123098.evelyn.moderation.messagefiltering;

import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.util.Cache;
import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;

import java.util.List;
import java.util.Map;

public class URLScanning {
    private static final Map<Message, List<Url>> MESSAGE_CACHE = new Cache<>(500, 5_000, message -> new UrlDetector(message.getContent(), UrlDetectorOptions.Default).detect());
    public static List<Url> getURLs(Message message) {
        return MESSAGE_CACHE.get(message);
    }
}
