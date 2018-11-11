package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Database;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.Time;

public class RecentGuildsCommand extends AbstractCommand {
    public RecentGuildsCommand() {
        super("recent guilds", ModuleLevel.BOT_ADMINISTRATIVE, "rguilds", "", "gets the number of or list of guilds within a certain time");
    }

    @Command
    public void command(MessageMaker maker, @Argument(optional = true) String time) {
        maker.getTitle().appendRaw("Recent guilds");
        Time time1 = new Time(time);
        long duration = System.currentTimeMillis() - time1.timeUntil();
        maker.appendRaw("" + getGuildActiveStats(duration));
        maker.send();
    }

    private int getGuildActiveStats(long time) {
        return Database.select("SELECT COUNT(*) FROM  WHERE value >= " + time, resultSet -> {
            resultSet.next();
            return resultSet.getInt(1);
        });
    }
}
