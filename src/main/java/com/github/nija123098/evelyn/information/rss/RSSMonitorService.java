package com.github.nija123098.evelyn.information.rss;

import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.service.AbstractService;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 6/18/2017.
 */
public class RSSMonitorService extends AbstractService {
    private static final Map<String, Date> LAST_UPDATED = new ConcurrentHashMap<>();
    public RSSMonitorService() {
        super(300_000);
        Launcher.registerShutdown(() -> ConfigHandler.setSetting(RSSLastCheckConfig.class, GlobalConfigurable.GLOBAL, LAST_UPDATED.values().stream().map(Date::getTime).reduce(Math::max).orElseGet(System::currentTimeMillis)));
    }// 5 min
    @Override
    public boolean mayBlock(){
        return true;
    }
    @Override
    public void run() {// RSSLastCheckConfig.getAndUpdate();
        Map<String, Set<Channel>> reverse = new HashMap<>();
        ConfigHandler.getNonDefaultSettings(RSSSubscriptionsConfig.class).forEach((channel, strings) -> strings.forEach(s -> reverse.computeIfAbsent(s, st -> new HashSet<>()).add(channel)));
        reverse.forEach((s, channels) -> {
            List<RSSNote> notes = getRSSNode(s);
            if (!notes.isEmpty()) notes.forEach(note -> channels.forEach(note::send));
        });
    }
    private static List<RSSNote> getRSSNode(String url){
        LAST_UPDATED.computeIfAbsent(url, s -> new Date());
        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
            if (feed.getEntries().isEmpty()) return Collections.emptyList();
            List<RSSNote> notes = new ArrayList<>();
            for (SyndEntry entry : feed.getEntries()){
                if (!entry.getPublishedDate().after(LAST_UPDATED.get(url))) break;
                notes.add(new RSSNote(entry));
            }
            LAST_UPDATED.get(url).setTime(feed.getEntries().get(0).getPublishedDate().getTime());
            return notes;
        } catch (FeedException | IOException ignored) {}// todo send a notification that a feed is corrupted
        return Collections.emptyList();
    }
    private static final String SAT = EmoticonHelper.getChars("satellite", false) + " ";
    private static class RSSNote {
        private MessageMaker maker = new MessageMaker((Channel) null);
        private RSSNote(SyndEntry entry) {
            this.maker.appendRaw(SAT + entry.getTitle() + "\n" + entry.getLink());
        }
        public void send(Channel channel){
            this.maker.withChannel(channel).clearMessage().send();
        }
    }
}
