package com.github.kaaz.emily.information.rss;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.service.AbstractService;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        map.forEach((channel, strings) -> strings.forEach(s -> {
            RSSNote note = getRSSNode(s);
            if (note != null) note.send(channel);
        }));
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
