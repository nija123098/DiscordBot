package com.github.nija123098.evelyn.information.rss;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.exception.ArgumentException;

import java.util.List;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RSSUnsubscribeCommand extends AbstractCommand {
    public RSSUnsubscribeCommand() {
        super(RSSCommand.class, "unsubscribe", null, null, "unsub", "Unsubs from an RSS feed");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Integer integer, @Argument(optional = true) String s, Channel channel, MessageMaker maker) {
        List<String> subs = ConfigHandler.getSetting(RSSSubscriptionsConfig.class, channel);
        if (integer == null) {
            if (!subs.remove(s)) maker.append("You were not subscribed to that.");
        } else {
            if (integer > subs.size()) throw new ArgumentException("You don't have that many subscriptions");
            subs.remove(integer - 1);
            ConfigHandler.setSetting(RSSSubscriptionsConfig.class, channel, subs);
        }
    }
}
