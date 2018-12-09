package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Database;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class StatsDetailedCommand extends AbstractCommand {

    public StatsDetailedCommand() {
        super(StatsCommand.class, "detailed", "advanced, more", null, null, "view detailed stats");
    }

    @Command
    public void command(MessageMaker maker, String s) {
        maker.getTitle().clear().appendRaw(EmoticonHelper.getChars("chart_with_upwards_trend",false) + " Evelyn Stats");
        maker.getNewFieldPart().withInline(true).withBoth("Total commands", "" + getCommandsUsed());
        maker.getNewFieldPart().withInline(true).withBoth("Total cookies", "" + getTotalCookies());
        maker.getNewFieldPart().withBoth("Guild stats", (StatsCommand.getTotalTable(s.startsWith("mini"))));
        long aMonthAgo = System.currentTimeMillis() - 2_592_000_000L, aWeekAgo = System.currentTimeMillis() - 604_800_000, oneDayAgo = System.currentTimeMillis() - 86_400_000;
        maker.getNewFieldPart().withBoth("Activity", ("\nActive Guilds: " + getGuildActiveStats(aMonthAgo))
                + ("  Recent users: " + getUserActiveStats(oneDayAgo))
                + ("  Active users: " + getUserActiveStats(aWeekAgo))
                + ("  Monthly users: " + getUserActiveStats(aMonthAgo)));

    }

    @Override
    public BotRole getBotRole() {
        return BotRole.CONTRIBUTOR;
    }

    private int getGuildActiveStats(long time) {
        return Database.select("SELECT COUNT(*) FROM guild_last_command_time_guild WHERE value >= " + time, resultSet -> {
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private int getUserActiveStats(long time) {
        return Database.select("SELECT COUNT(*) FROM last_command_time_user WHERE value >= " + time, resultSet -> {
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private int getCommandsUsed() {
        return Database.select("SELECT SUM(value) FROM `commands_used_count_user` WHERE id NOT LIKE '484445339764523009' AND id NOT LIKE '453676619601543169'", resultSet -> {
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private int getTotalCookies() {
        return Database.select("SELECT SUM(value) FROM `current_currency_user`", resultSet -> {
           resultSet.next();
           return resultSet.getInt(1);
        });
    }
}
