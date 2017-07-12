package com.github.kaaz.emily.information.rss;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.exeption.ArgumentException;

import java.util.List;

/**
 * Made by nija123098 on 7/7/2017.
 */
public class RSSUnsubscribeCommand extends AbstractCommand {
    public RSSUnsubscribeCommand() {
        super(RSSCommand.class, "unsubscribe", "unsub", null, null, "Unsubs from an RSS feed");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Integer integer, @Argument(optional = true) String s, Channel channel, MessageMaker maker){
        List<String> subs = ConfigHandler.getSetting(RSSSubscriptionsConfig.class, channel);
        if (integer == null){
            if (!subs.remove(s)) maker.append("You were not subscribed to that.");
        } else {
            if (integer > subs.size()) throw new ArgumentException("You don't have that many subscriptions");
            subs.remove((int) integer);// Intellij is saying it still uses the Object arg method
        }
        ConfigHandler.setSetting(RSSSubscriptionsConfig.class, channel, subs);
    }
}
