package com.github.nija123098.evelyn.information.stats;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class TopCommandsCommand extends AbstractCommand {

    public TopCommandsCommand() {
        super(TopStatsCommand.class, "commands", "tfc", null, null, "get the top 5 command users");
    }

    @Command
    public void command(MessageMaker maker, @Argument(optional = true, replacement = ContextType.NONE) Integer count) {
        if (count == null) count = 5;
        Map<User, Integer> sortedMap = TopStatsCommand.getStats("commands_used_count_user", count).entrySet().stream().sorted(Map.Entry.<User, Integer>comparingByValue().reversed())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        List<Map.Entry<User, Integer>> list = new LinkedList<>(sortedMap.entrySet());
        maker.getTitle().appendRaw("Top 5 Users: Commands");
        for (int i = 0; i < 5; i++) {
            maker.getNewListPart().appendRaw(list.get(i).getKey().getNameAndDiscrim() + " | " + list.get(i).getKey().getID() + "\n" + list.get(i).getValue() + " commands");
        }
    }
}