package com.github.nija123098.evelyn.botmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * @author Soarnir
 * @since 1.0.0
*/
public class FixCommand extends AbstractCommand {

    public FixCommand() {
            super("fix", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Fix a user (grant them admin botroles)");
    }

    @Command
    public void command(@Argument User user, Guild guild, MessageMaker maker) {
        BotRole.setRole(BotRole.BOT_ADMIN, true, user, guild);
        BotRole.setRole(BotRole.CONTRIBUTOR, true, user, guild);
        maker.appendRaw("Fixed " + user.mention());
    }
}