package com.github.nija123098.evelyn.information.stats;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Database;
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
        Map<User, Integer> userCommandCount = getStats();
        List<Map.Entry<User, Integer>> list = new LinkedList<>(userCommandCount.entrySet());
        maker.mustEmbed();
        maker.getTitle().appendRaw("Top 5 Users: Commands");
        for (int i = 0; i < 5; i++) {
            maker.getNewFieldPart().withInline(false).withBoth(list.get(i).getKey().getNameAndDiscrim(),list.get(i).getValue() + " commands");
        }
    }

    private Map<User, Integer> getStats() {
        return Database.select("SELECT * FROM commands_used_count_user ORDER BY VALUE DESC", set -> {
            Map<User, Integer> temp = new ConcurrentHashMap<>();
            for (int i = 1; i < 6; i++) {
                set.next();
                temp.put((User.getUser(set.getString(1)) != null ? (User.getUser(set.getString(1))) : DiscordClient.getOurUser()), set.getInt(2));
            }
            return temp;
        });
    }
}