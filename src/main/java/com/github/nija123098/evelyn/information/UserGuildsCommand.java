package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Made by nija123098 on 6/12/2017.
 */
public class UserGuildsCommand extends AbstractCommand {
    public UserGuildsCommand() {
        super(UserCommand.class, "guilds", null, null, null, "Shows the guilds a user is currently in");
    }
    @Command
    public void command(@Argument User user, MessageMaker maker){
        Set<Guild> guilds = user.getGuilds();
        List<List<String>> stats = new ArrayList<>(guilds.size());
        guilds.forEach(guild -> {
            List<String> stat = new ArrayList<>();
            stat.add(guild.getShard().getID() + "");
            stat.add(guild.getID());
            stat.add(guild.getName());
            stats.add(stat);
        });
        maker.appendRaw(FormatHelper.makeAsciiTable(Arrays.asList("shard", "guild", "name"), stats, null));
    }
    @Override
    public BotRole getBotRole() {
        return BotRole.BOT_ADMIN;
    }
}
