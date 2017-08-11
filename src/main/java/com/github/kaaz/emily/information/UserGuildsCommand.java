package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.FormatHelper;

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
}
