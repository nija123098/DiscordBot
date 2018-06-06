package com.github.nija123098.evelyn.moderation.messagefiltering.filters;

import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageFilter;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringException;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringUrlWhitelist;
import com.github.nija123098.evelyn.util.Log;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;

import java.util.regex.Pattern;

public class URLFilter implements MessageFilter {
    public static MessageMonitoringUrlWhitelist WHITELIST;
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        String periodEscaped = Pattern.quote(".");
        new UrlDetector(event.getMessage().getContent(), UrlDetectorOptions.Default).detect().forEach(url -> {
            if (WHITELIST.getValue(event.getGuild()).contains(url.getHost())) return;
            String[] strings = url.getHost().split(periodEscaped);
            if (strings.length == 1) {
                Log.log("1 part URL, unable to handle, skipping URLFiltering for it: " + url.getFullUrl());
                return;
            }
            String domain = strings.length == 2 ? url.getHost() : strings[strings.length - 2] + "." + strings[strings.length - 1];
            if (WHITELIST.getValue(event.getGuild()).contains(domain)) return;
            String compose = url.getHost() + url.getPath();
            for (String path : WHITELIST.getValue(event.getGuild())) {
                if (compose.startsWith(path)) return;
            }
            throw new MessageMonitoringException("Illegal URL: " + url, true);
        });
    }

    @Override
    public MessageMonitoringLevel getType() {
        return MessageMonitoringLevel.URL;
    }
}
