package com.github.kaaz.emily.information.rss;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.util.FormatHelper;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<List<String>> body = new ArrayList<>(subs.size());
        AtomicInteger i = new AtomicInteger(-1);
        subs.forEach(s -> body.add(Arrays.asList(i.incrementAndGet() + "", s)));
        maker.appendRaw(FormatHelper.makeAsciiTable(Arrays.asList("NUMBER", "URL"), body, null));
    }
}
