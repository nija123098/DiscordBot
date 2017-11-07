package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.List;

/**
 * Made by nija123098 on 8/9/2017.
 */
public class WhoAreCommand extends AbstractCommand {
    public WhoAreCommand() {
        super("whoare", ModuleLevel.HELPER, "who are, inquire role", null, "Shows the users that have the given role");
    }
    @Command
    public void command(@Argument Role role, Guild guild, MessageMaker maker){
        List<User> users = role.getUsers();
        maker.getTitle().appendRaw("Users with the " + role.getName() + " role");
        maker.append("There " + (users.size() > 1 ? "are " + users.size() + " users" : "is 1 user") + " with that role").appendRaw("\n");
        maker.withColor(role);
        users.stream().map(user -> {
            String nick = user.getNickname(guild);
            return nick == null ? user.getNameAndDiscrim() : nick + " AKA " + user.getNameAndDiscrim();
        }).forEach(s -> maker.getNewListPart().appendRaw(s));
    }
}
