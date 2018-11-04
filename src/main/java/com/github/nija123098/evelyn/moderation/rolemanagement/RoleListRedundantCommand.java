package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleListRedundantCommand extends AbstractCommand {

    public RoleListRedundantCommand() {
        super(RoleCommand.class,"list redundant", "rlist redundant", null, null, "Show a list of all roles with no users");
    }

    @Command
    public void command(Guild guild, MessageMaker maker) {
        List<Role> roles = guild.getRoles().stream().filter(role -> role.getUsers().size() == 0).collect(Collectors.toList());
        final int[] counter = {0};
        maker.getTitle().appendRaw(guild.getName() + " | " + roles.size() + " roles");
        maker.getNewListPart().appendRaw("\u200b");
        maker.getNewListPart().appendRaw("`# " + "|role name`");
        Collections.reverse(roles);
        roles.forEach(role -> {
            maker.getNewListPart().appendRaw("`0 |`" + role.getName());
            if (counter[0] == 35) {
                maker.guaranteeNewListPage();
                counter[0] = 0;
            }
            counter[0]++;
        });
    }
}