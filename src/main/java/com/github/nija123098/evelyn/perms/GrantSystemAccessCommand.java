package com.github.nija123098.evelyn.perms;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.launcher.Launcher;

public class GrantSystemAccessCommand extends AbstractCommand {
    public GrantSystemAccessCommand() {
        super("grant system access", BotRole.BOT_OWNER, ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Grants the user system access");
    }
    @Command
    public void command(@Argument(optional = true) User user){
        Launcher.grantSystemAccess(user);
    }
}
