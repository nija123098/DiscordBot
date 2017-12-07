package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;

import java.util.Collections;
import java.util.List;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleListCommand extends AbstractCommand {

	//constructor
	public RoleListCommand() {
		super(RoleCommand.class,"list", "rlist", null, null, "Show a list of all roles in a server");
	}

	@Command
	public void command(Guild guild, MessageMaker maker) {
		final int[] largestRole = {0};
		guild.getRoles().forEach(role -> {
			if (role.getUsers().size() > largestRole[0]) largestRole[0] = role.getUsers().size();
		});
		List<Role> roles = guild.getRoles();
		int magnitude = String.valueOf(largestRole[0]).length();
		String[] zeroes = new String[magnitude];
		String zero = "";
		for (int l = magnitude; l > 0; l--) {
			zeroes[l - 1] = zero;
			zero = zero + "0";
		}
		final int[] counter = {0};
		maker.getTitle().appendRaw("Role list for " + guild.getName());
		maker.getNewListPart().appendRaw("\u200b");
		maker.getNewListPart().appendRaw("`# " + zeroes[0].replace('0', ' ') + "|role name`");
		Collections.reverse(roles);
		roles.forEach(role -> {
			maker.getNewListPart().appendRaw("`" + zeroes[String.valueOf(role.getUsers().size()).length() - 1] + role.getUsers().size() + " |`" + role.getName());
			if (counter[0] == 35) {
				maker.guaranteeNewListPage();
				counter[0] = 0;
			}
			counter[0]++;
		});
	}
}