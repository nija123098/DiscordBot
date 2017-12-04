package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class UserListCommand extends AbstractCommand {
    public UserListCommand() {
        super(UserCommand.class, "list", null, null, null, "Shows the user list for the guild, sorted by join count");
    }

    @Command
    public void command( @Context(softFail = true) Guild guild, MessageMaker maker) {
        int userCount = guild.getUsers().size();
        String[] users = new String[userCount];
        int count = 0;
        int magnitude = String.valueOf(userCount).length();
        String[] zeroes = new String[magnitude];
        String zero = "";
        for (int l = magnitude; l > 0; l--) {
            zeroes[l - 1] = zero;
            zero = zero + "0";
        }
        maker.getTitle().appendRaw(guild.getName() + " | " + guild.getUserSize() + " users");
        for (User user : guild.getUsers()) {
            GuildUser guildUser = GuildUser.getGuildUser(guild, user);
            users[guildUser.getJoinPosition()] = "`" + zeroes[String.valueOf(guildUser.getJoinPosition() + 1).length() - 1] + (guildUser.getJoinPosition() + 1) + " |` " + guildUser.getName();
            count++;
        }
        int counter = 0;
        for (String name : users) {
            maker.getNewListPart().appendRaw(name);
            if (counter == 35) {
                maker.guaranteeNewListPage();
                counter = 0;
            }
            counter++;
        }
    }
}
