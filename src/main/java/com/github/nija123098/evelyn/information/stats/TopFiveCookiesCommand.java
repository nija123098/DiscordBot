package com.github.nija123098.evelyn.information.stats;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Database;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class TopFiveCookiesCommand extends AbstractCommand {

    public TopFiveCookiesCommand() {
        super(TopFiveCommand.class,"cookies", null, null, null, "gets the top five cookie collectors");
    }

    @Command
    public void command(MessageMaker maker) {
        Map<User, Integer> sortedMap = getStats().entrySet().stream().sorted(Map.Entry.<User, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        List<Map.Entry<User, Integer>> list = new LinkedList<>(sortedMap.entrySet());
        maker.mustEmbed();
        maker.getTitle().appendRaw("Top 5 Users: Cookies");
        for (int i = 0; i < 5; i++) {
            maker.getNewListPart().appendRaw(list.get(i).getKey().getNameAndDiscrim() + " | " + list.get(i).getKey().getID() + "\n" + list.get(i).getValue() + " cookies");
        }
    }

    private Map<User, Integer> getStats() {
        return Database.select("SELECT * FROM current_currency_user ORDER BY VALUE DESC", set -> {
            Map<User, Integer> temp = new HashMap<>();
            for (int i = 1; i < 6; i++) {
                set.next();
                if (Objects.equals(User.getUser(set.getString(1)), DiscordClient.getOurUser())) set.next();
                temp.put((User.getUser(set.getString(1)) != null ? (User.getUser(set.getString(1))) : DiscordClient.getOurUser()), set.getInt(2));
            }
            return temp;
        });
    }
}