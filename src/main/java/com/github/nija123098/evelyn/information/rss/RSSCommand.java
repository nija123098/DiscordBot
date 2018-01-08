package com.github.nija123098.evelyn.information.rss;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class RSSCommand extends AbstractCommand {
    public RSSCommand() {
        super("rss", ModuleLevel.ADMINISTRATIVE, null, null, "Displays all current subscribed rss feeds");
    }
    @Command
    public void command(Channel channel, MessageMaker maker){
        List<String> subs = ConfigHandler.getSetting(RSSSubscriptionsConfig.class, channel);
        if (subs.isEmpty()){
            maker.append("You aren't currently subscribed to any rss feeds.");
            return;
        }
        AtomicInteger i = new AtomicInteger(0);
        subs.forEach(s -> maker.getNewListPart().appendRaw(i.incrementAndGet() + " " + s));
    }
}
