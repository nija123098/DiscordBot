package com.github.nija123098.evelyn.perms;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class BotRoleSetCommand extends AbstractCommand {
    public BotRoleSetCommand() {
        super(BotRoleCommand.class, "set", null, null, null, "Adds or removes bot roles from a user");
    }
    @Command
    public void command(User setter, @Argument User user, @Argument Boolean add, @Argument BotRole role, Guild guild, MessageMaker maker){
        BotRole.setRole(role, add, user, setter, guild);
        BotRoleCommand.command(user, guild, maker);
    }
}
