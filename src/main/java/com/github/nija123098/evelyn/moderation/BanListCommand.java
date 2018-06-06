package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

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
        maker.getTitle().appendRaw("Banned Users");
        guild.getBannedUsers().forEach(user -> maker.getNewListPart().append(user.getNameAndDiscrim() + " " + user.getID()));
    }
}