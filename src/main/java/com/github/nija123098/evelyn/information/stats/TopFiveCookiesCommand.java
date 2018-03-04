package com.github.nija123098.evelyn.information.stats;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;

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
        Map<User, Integer> userCookieCount = new ConcurrentHashMap<>();
        DiscordClient.getUsers().forEach(user1 -> userCookieCount.put(user1, ConfigHandler.getSetting(CurrentCurrencyConfig.class, user1)));
        List<Map.Entry<User, Integer>> list = new LinkedList<>(userCookieCount.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
        maker.mustEmbed();
        maker.getTitle().appendRaw("Top 5 Users: Cookies");
        for (int i = 0; i < 5; i++) {
            maker.getNewFieldPart().withInline(false).withBoth(list.get(i).getKey().getNameAndDiscrim(),list.get(i).getValue() + " cookies");
        }
    }
}