package com.github.kaaz.emily.information.rss;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class RSSSubscribeCommand extends AbstractCommand {
    public RSSSubscribeCommand() {
        super(RSSCommand.class, "subscribe", null, null, "sub", "Subscribes to an rss feed");
    }
    @Command
    public void command(Channel channel, @Argument(info = "The rss to subscribe to") String arg, MessageMaker maker){
        try{
            new SyndFeedInput().build(new XmlReader(new URL(arg)));
            maker.withOK();
        } catch (FeedException | IOException e) {
            maker.append("Invalid rss feed: " + arg);
        }
    }
}
