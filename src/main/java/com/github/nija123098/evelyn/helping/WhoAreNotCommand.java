package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.List;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class WhoAreNotCommand extends AbstractCommand {
    public WhoAreNotCommand() {
        super(WhoAreCommand.class, "not", "whoarenot, whoarnt", null, null, "Shows the users that do not have the given role");
    }
    @Command
    public void command(@Argument Role role, Guild guild, MessageMaker maker){
        List<User> users = guild.getUsers();
        users.removeAll(role.getUsers());
        maker.getTitle().appendRaw("Users without the " + role.getName() + " role");
        maker.append("There " + (users.size() > 1 ? "are " + users.size() + " users" : "is 1 user") + " without that role").appendRaw("\n");
        maker.withColor(role);
        users.stream().map(user -> {
            String nick = user.getNickname(guild);
            return nick == null ? user.getNameAndDiscrim() : nick + " AKA " + user.getNameAndDiscrim();
        }).forEach(s -> maker.getNewListPart().appendRaw(s));
    }
}
