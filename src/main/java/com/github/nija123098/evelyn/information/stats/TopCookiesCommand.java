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
public class TopCookiesCommand extends AbstractCommand {

    public TopCookiesCommand() {
        super(TopStatsCommand.class, "cookies", null, null, null, "gets the top five cookie collectors");
    }

    @Command
    public void command(MessageMaker maker, @Argument(optional = true, replacement = ContextType.NONE) Integer count) {
        if (count == null) count = 5;
        Map<User, Integer> sortedMap = TopStatsCommand.getStats("current_currency_user", count).entrySet().stream().sorted(Map.Entry.<User, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        List<Map.Entry<User, Integer>> list = new LinkedList<>(sortedMap.entrySet());
        maker.getTitle().appendRaw("Top 5 Users: Cookies");
        for (int i = 0; i < (list.size() > 10 ? count : list.size()); i++) {
            maker.getNewListPart().appendRaw(list.get(i).getKey().getNameAndDiscrim() + " | " + list.get(i).getKey().getID() + "\n" + list.get(i).getValue() + " cookies");
        }
    }
}