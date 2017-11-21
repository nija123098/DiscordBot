package com.github.nija123098.evelyn.information.rss;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.NetworkHelper;
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
    public void command(@Argument(info = "The rss to subscribe to") String arg, Channel channel, MessageMaker maker){
        if (!NetworkHelper.isValid(arg)) throw new ArgumentException("That isn't a valid link");
        try{new SyndFeedInput().build(new XmlReader(new URL(arg)));
            ConfigHandler.alterSetting(RSSSubscriptionsConfig.class, channel, strings -> strings.add(arg));
        } catch (FeedException e) {
            maker.append("That's not a valid RSS feed!" + arg);
        } catch (IOException e) {
            throw new DevelopmentException("Error getting rss feed", e);
        }
    }
}
