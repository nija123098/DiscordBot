package com.github.nija123098.evelyn.information.stats;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Database;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.Log;

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
            int count = 0;
            while (set.next()) {
                ++count;
            }
            set.beforeFirst();
            Map<User, Integer> temp = new HashMap<>();
            for (int i = 0; i < (count < number ? count : number); i++) {
                set.next();
                if (Objects.equals(User.getUser(set.getString(1)), DiscordClient.getOurUser()) || User.getUser(set.getString(1)) == null) {
                    set.next();
                    i--;
                }
                temp.put((User.getUser(set.getString(1))), set.getInt(2));
            }
            return temp;
        });
    }
}