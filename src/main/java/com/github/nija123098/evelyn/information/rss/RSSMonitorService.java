package com.github.nija123098.evelyn.information.rss;

import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.service.AbstractService;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Log;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RSSMonitorService extends AbstractService {
    private static final Map<String, Date> LAST_UPDATED = new ConcurrentHashMap<>();
    public RSSMonitorService() {
        super(300_000);
        Launcher.registerShutdown(() -> ConfigHandler.setSetting(RSSLastCheckConfig.class, GlobalConfigurable.GLOBAL, LAST_UPDATED.values().stream().map(Date::getTime).reduce(Math::max).orElseGet(System::currentTimeMillis)));
    }// 5 min
    @Override
    public boolean mayBlock() {
        return true;
    }
    @Override
    public void run() {// RSSLastCheckConfig.getAndUpdate();
        Map<String, Set<Channel>> reverse = new HashMap<>();
        ConfigHandler.getNonDefaultSettings(RSSSubscriptionsConfig.class).forEach((channel, strings) -> strings.forEach(s -> reverse.computeIfAbsent(s, st -> new HashSet<>()).add(channel)));
        reverse.forEach((s, channels) -> {
            List<RSSNote> notes = getRSSNode(s);
            Collections.reverse(notes);
            notes.forEach(note -> channels.forEach(note::send));
        });
    }
    private static List<RSSNote> getRSSNode(String url) {
        LAST_UPDATED.computeIfAbsent(url, s -> new Date());
        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
            List<SyndEntry> entries = feed.getEntries();
            if (entries.isEmpty()) return Collections.emptyList();
            List<RSSNote> notes = entries.stream().filter(syndEntry -> syndEntry.getPublishedDate().after(LAST_UPDATED.get(url))).map(RSSNote::new).collect(Collectors.toList());
            LAST_UPDATED.get(url).setTime(entries.get(0).getPublishedDate().getTime());
            return notes;//               0 is the most recent.
        } catch (Exception e) {
            Log.log("Possibly failed feed, disabled for 1 day: " + url, e);
            LAST_UPDATED.get(url).setTime(System.currentTimeMillis() + 86_400_000);
        }// todo add automatic/manual review and post notice to subscribed channels
        return Collections.emptyList();
    }
    private static final String SAT = EmoticonHelper.getChars("satellite", false) + " ";
    private static class RSSNote {
        private MessageMaker maker = new MessageMaker((Channel) null);
        private RSSNote(SyndEntry entry) {
            this.maker.appendRaw(SAT + entry.getTitle() + "\n" + entry.getLink());
        }
        public void send(Channel channel) {
            this.maker.withChannel(channel).clearMessage().send();
        }
    }
}
