package com.github.nija123098.evelyn.perms;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.launcher.Launcher;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GrantSystemAccessCommand extends AbstractCommand {
    public GrantSystemAccessCommand() {
        super("grant system access", BotRole.BOT_ADMIN, ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Grants the user system access");
    }
    @Command
    public void command(@Argument(optional = true) User user){
        Launcher.grantSystemAccess(user);
    }
}
