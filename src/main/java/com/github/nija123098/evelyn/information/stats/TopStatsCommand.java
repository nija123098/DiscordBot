package com.github.nija123098.evelyn.information.stats;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Database;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class TopStatsCommand extends AbstractCommand {

    public TopStatsCommand() {
        super("topstats", ModuleLevel.BOT_ADMINISTRATIVE, "tf, topfive, ts", null, "get the top five command users");
    }

    @Command
    public void command(MessageMaker maker) {
        maker.getTitle().append("Top Options").append("Please select from one of the following commands:");
        this.getSubCommands().forEach(abstractCommand -> maker.getNewListPart().appendRaw(abstractCommand.getName()));
    }

    static Map<User, Integer> getStats(String table, int number) {
        return Database.select("SELECT * FROM " + table + " ORDER BY VALUE DESC", set -> {
            Map<User, Integer> temp = new HashMap<>();
            for (int i = 0; i < number; i++) {
                set.next();
                if (Objects.equals(User.getUser(set.getString(1)), DiscordClient.getOurUser())) set.next();
                temp.put((User.getUser(set.getString(1)) != null ? (User.getUser(set.getString(1))) : DiscordClient.getOurUser()), set.getInt(2));
            }
            return temp;
        });
    }
}