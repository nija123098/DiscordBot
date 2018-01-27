package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class BanListCommand extends AbstractCommand {

    public BanListCommand() {
        super("banlist", ModuleLevel.ADMINISTRATIVE, "bl", null, "Get a list of the current banned users for the server");
    }

    @Command
    public void command(@Context(softFail = true) Guild guild, MessageMaker maker) {
        int userCounter = 1;
        maker.getTitle().appendRaw(guild.getName() + " | " + guild.getBannedUsers().size() + " banned users");
        maker.getNewListPart().appendRaw("\u200b");
        List<List<String>> body = new ArrayList<>();
        for (User user : guild.getBannedUsers()) {
            body.add(Arrays.asList(user.getNameAndDiscrim(), user.getID()));
            if (userCounter == 15) {
                maker.getNewFieldPart().withBoth("\u200b", (FormatHelper.makeAsciiTable(Arrays.asList("User", "ID"), body, null)));
                maker.guaranteeNewFieldPage();
                userCounter = 0;
                body.clear();
            }
            userCounter++;
        }
    }
}