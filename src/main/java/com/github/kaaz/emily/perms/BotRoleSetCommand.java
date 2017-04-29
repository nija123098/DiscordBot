package com.github.kaaz.emily.perms;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Convert;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class BotRoleSetCommand extends AbstractCommand {
    public BotRoleSetCommand() {
        super(BotRoleCommand.class, "set", null, null, null, "Adds or removes bot roles from a user");
    }
    @Command
    public void command(User setter, @Convert User user, @Convert Boolean add, @Convert BotRole role, Guild guild){
        BotRole.setRole(role, add, user, setter, guild);
    }
}
