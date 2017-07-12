package com.github.kaaz.emily.information.rss;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;

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
