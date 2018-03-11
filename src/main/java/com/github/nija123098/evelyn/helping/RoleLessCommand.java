package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleLessCommand extends AbstractCommand {

    public RoleLessCommand() {
        super("roleless", ModuleLevel.ADMINISTRATIVE, null, null, "check which users have no roles in the server");
    }

    @Command
    public void command(Guild guild, MessageMaker maker) {
        List<User> users = guild.getUsers().stream().filter(user -> user.getRolesForGuild(guild).size() == 1).collect(Collectors.toList());
        maker.getTitle().appendRaw("Users without roles");
        if (users.size() > 0) {
            maker.append("There " + (users.size() > 1 ? "are " + users.size() + " users" : "is 1 user") + " without roles").appendRaw("\n");
            users.stream().map(user -> {
                String nick = user.getNickname(guild);
                return nick == null ? user.getNameAndDiscrim() : nick + " AKA " + user.getNameAndDiscrim();
            }).forEach(s -> maker.getNewListPart().appendRaw(s));
        } else {
            maker.appendRaw("There are no users without roles.");
        }
    }
}