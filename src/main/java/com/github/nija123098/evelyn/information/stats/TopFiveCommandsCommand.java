package com.github.nija123098.evelyn.information.stats;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Database;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
        Map<User, Integer> sortedMap = getStats().entrySet().stream().sorted(Map.Entry.<User, Integer>comparingByValue().reversed())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        List<Map.Entry<User, Integer>> list = new LinkedList<>(sortedMap.entrySet());
        maker.mustEmbed();
        maker.getTitle().appendRaw("Top 5 Users: Commands");
        for (int i = 0; i < 5; i++) {
            maker.getNewListPart().appendRaw(list.get(i).getKey().getNameAndDiscrim() + " | " + list.get(i).getKey().getID() + "\n" + list.get(i).getValue() + " commands");
        }
    }

    private Map<User, Integer> getStats() {
        return Database.select("SELECT * FROM commands_used_count_user ORDER BY VALUE DESC", set -> {
            Map<User, Integer> temp = new HashMap<>();
            for (int i = 1; i < 6; i++) {
                set.next();
                temp.put((User.getUser(set.getString(1)) != null ? (User.getUser(set.getString(1))) : DiscordClient.getOurUser()), set.getInt(2));
            }
            return temp;
        });
    }
}