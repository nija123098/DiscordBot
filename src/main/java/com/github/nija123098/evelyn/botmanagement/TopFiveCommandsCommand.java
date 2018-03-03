package com.github.nija123098.evelyn.botmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.configs.CommandsUsedCountConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class TopFiveCommandsCommand extends AbstractCommand {

    public TopFiveCommandsCommand() {
        super(TopFiveCommand.class, "commands", "tfc", null, null, "get the top 5 command users");
    }

    @Command
    public void command(MessageMaker maker) {
        Map<User, Integer> userCommandCount = new ConcurrentHashMap<>();
        DiscordClient.getUsers().forEach(user1 -> userCommandCount.put(user1, ConfigHandler.getSetting(CommandsUsedCountConfig.class, user1)));
        List<Map.Entry<User, Integer>> list = new LinkedList<>(userCommandCount.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
        maker.mustEmbed();
        maker.getTitle().appendRaw("Top 5 Users: Commands");
        for (int i = 0; i < 5; i++) {
            maker.getNewFieldPart().withInline(false).withBoth(list.get(i).getKey().getNameAndDiscrim(),list.get(i).getValue() + " commands");
        }
    }
}