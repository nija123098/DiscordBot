package com.github.nija123098.evelyn.information.subsription;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.information.configs.SubscriptionsConfig;

/**
 * Made by nija123098 on 5/24/2017.
 */
public class SubscribeCommand extends AbstractCommand {
    public SubscribeCommand() {
        super("subscribe", ModuleLevel.ADMINISTRATIVE, "sub", null, "Subscribe this channel to certain events, take a look at @Evelyn rss");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE, info = "The thing to subscribe to") SubscriptionLevel level, Channel channel, MessageMaker maker){
        if (level == null) SubscribeCurrentCommand.command(channel, maker);
        else ConfigHandler.alterSetting(SubscriptionsConfig.class, channel, levels -> levels.add(level));
    }
}
