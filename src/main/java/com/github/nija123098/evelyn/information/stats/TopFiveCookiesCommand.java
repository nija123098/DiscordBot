package com.github.nija123098.evelyn.information.stats;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Database;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        Map<User, Integer> userCookieCount = getStats();
        List<Map.Entry<User, Integer>> list = new LinkedList<>(userCookieCount.entrySet());
        maker.mustEmbed();
        maker.getTitle().appendRaw("Top 5 Users: Cookies");
        for (int i = 0; i < 5; i++) {
            maker.getNewFieldPart().withInline(false).withBoth(list.get(i).getKey().getNameAndDiscrim(),list.get(i).getValue() + " cookies");
        }
    }

    private Map<User, Integer> getStats() {
        return Database.select("SELECT * FROM current_currency_user ORDER BY value", set -> {
            Map<User, Integer> temp = new ConcurrentHashMap<>();
            for (int i = 1; i < 6; i++) {
                set.next();
                temp.put(User.getUser(set.getString(1)), set.getInt(2));
            }
            return temp;
        });
    }
}