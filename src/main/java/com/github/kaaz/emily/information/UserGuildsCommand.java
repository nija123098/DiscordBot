package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.FormatHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Made by nija123098 on 6/12/2017.
 */
public class UserGuildsCommand extends AbstractCommand {
    public UserGuildsCommand() {
        super(UserCommand.class, "guilds", null, null, null, "Shows the guilds a user is currently in");
    }
    @Command
    public void command(@Argument User user, MessageMaker maker){
        List<String> shards = new ArrayList<>(), IDs = new ArrayList<>(), names = new ArrayList<>();
        for (Guild guild : user.getGuilds()){
            shards.add(guild.getShard().getID() + "");
            IDs.add(guild.getID());
            names.add(guild.getName());
        }
        maker.append(FormatHelper.makeAsciiTable(Arrays.asList("shard", "guild", "name"), Arrays.asList(shards, IDs, names), null));
    }
}
