package com.github.nija123098.evelyn.information.subsription;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SubscribeCurrentCommand extends AbstractCommand {
    public SubscribeCurrentCommand() {
        super(SubscribeCommand.class, "current", "subscriptions", null, null, "Shows the current subscriptions for the channel");
    }
    @Command
    public static void command(Channel channel, MessageMaker maker) {
        Set<SubscriptionLevel> levels = ConfigHandler.getSetting(SubscriptionsConfig.class, channel);
        if (levels.isEmpty()) maker.append("You are currently not subscribed to anything in this channel, use `@Evelyn subscribe info` to find possible subscriptions, or use @Evelyn rss");
        else {
            maker.append("You are currently subscribed to the following:\n").appendRaw("```\n");
            levels.forEach(subscriptionLevel -> maker.appendRaw(subscriptionLevel.name() + "\n"));
            maker.appendRaw("```");
        }
    }
}
