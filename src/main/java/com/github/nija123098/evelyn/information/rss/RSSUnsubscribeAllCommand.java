package com.github.nija123098.evelyn.information.rss;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.util.ArrayList;

/**
 * Made by nija123098 on 7/7/2017.
 */
public class RSSUnsubscribeAllCommand extends AbstractCommand {
    public RSSUnsubscribeAllCommand() {
        super(RSSUnsubscribeCommand.class, "all", null, null, null, "Unsubs all rss feeds");
    }
    @Command
    public void command(Channel channel){
        ConfigHandler.setSetting(RSSSubscriptionsConfig.class, channel, new ArrayList<>(0));
    }
}
