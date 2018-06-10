package com.github.nija123098.evelyn.moderation.messagefiltering.filters;

import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.moderation.logging.ModLogConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.*;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringUrlBlacklist;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringUrlWhitelist;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.Log;
import com.linkedin.urls.Url;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class URLFilter implements MessageFilter {
    public static MessageMonitoringUrlBlacklist BLACKLIST;
    public static MessageMonitoringUrlWhitelist WHITELIST;
    private static final String PERIOD_ESCAPED = Pattern.quote(".");

    @Override
    public void checkFilter(DiscordMessageReceived event) {
        Set<String> whitelist = WHITELIST.getValue(event.getGuild());
        Set<String> blacklist = BLACKLIST.getValue(event.getGuild());
        List<Url> list = URLScanning.getURLs(event.getMessage()).stream().filter(url -> {
            if (!whitelist.isEmpty() && check(whitelist, url)) return false;
            if (blacklist.isEmpty()) {
                throw new MessageMonitoringException("Only whitelisted URLs are allowed here", true);
            } else if (check(blacklist, url)) {
                throw new MessageMonitoringException("Illegal URL: " + url, true);
            }
            return true;
        }).collect(Collectors.toList());
        if (list.isEmpty() || !(!whitelist.isEmpty() && !blacklist.isEmpty())) return;
        MessageMaker maker = new MessageMaker(ConfigHandler.getSetting(ModLogConfig.class, event.getGuild()));
        maker.getTitle().append("URL warning");
        maker.append("Use the reaction to enact moderation within the time.\nYou should add the relevant url part to the blacklist if necessary.");
        list.forEach(url -> maker.getNewListPart().appendRaw(url.getFullUrl()));
        maker.withPublicReactionBehavior("no_entry_sign", (add, reaction, user) -> {
            if (!BotRole.GUILD_TRUSTEE.hasRequiredRole(user, event.getGuild())) return;
            MessageMonitor.moderate(event, new MessageMonitoringException("Illegal URL", true));
        });
        maker.send();
    }

    private boolean check(Set<String> list, Url url) {
        if (list.contains(url.getHost())) return true;
        String[] strings = url.getHost().split(PERIOD_ESCAPED);
        if (strings.length == 1) {
            Log.log("1 part URL, unable to handle, skipping URLFiltering for it: " + url.getFullUrl());
            return false;
        }
        String domain = strings.length == 2 ? url.getHost() : strings[strings.length - 2] + "." + strings[strings.length - 1];
        if (list.contains(domain)) return true;
        String compose = url.getHost() + url.getPath();
        for (String path : list) {
            if (compose.startsWith(path)) return true;
        }
        return false;
    }

    @Override
    public MessageMonitoringLevel getType() {
        return MessageMonitoringLevel.URL;
    }
}
