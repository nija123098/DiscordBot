package com.github.nija123098.evelyn.information.rss;

import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
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

/**
 * Made by nija123098 on 6/18/2017.
 */
public class RSSMonitorService extends AbstractService {
    private static final Date DATE = new Date();
    public RSSMonitorService() {
        super(300_000);
    }// 5 min
    @Override
    public boolean mayBlock(){
        return true;
    }
    @Override
    public void run() {
        DATE.setTime(RSSLastCheckConfig.getAndUpdate());
        Map<Channel, List<String>> map = ConfigHandler.getNonDefaultSettings(RSSSubscriptionsConfig.class);
        Map<String, Set<Channel>> reverse = new HashMap<>();
        map.forEach((channel, strings) -> strings.forEach(s -> reverse.computeIfAbsent(s, st -> new HashSet<>()).add(channel)));
        reverse.forEach((s, channels) -> {
            RSSNote note = getRSSNode(s);
            if (note != null) channels.forEach(note::send);
        });
    }
    private static RSSNote getRSSNode(String url){
        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
            SyndEntry entry = feed.getEntries().get(0);
            if (entry.getPublishedDate().after(DATE)) return new RSSNote(entry);
        } catch (FeedException | IOException ignored) {}
        return null;
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
