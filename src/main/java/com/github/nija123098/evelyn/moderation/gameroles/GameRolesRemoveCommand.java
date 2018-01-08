package com.github.nija123098.evelyn.moderation.gameroles;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;

public class GameRolesRemoveCommand extends AbstractCommand {
    public GameRolesRemoveCommand() {
        super(GameRolesCommand.class, "remove", null, null, null, "Removes a role to the list of roles that will added to the user while they play a game");
    }
    @Command
    public void command(@Argument Role role){
        ConfigHandler.setSetting(GameRoleConfig.class, role, false);
    }
}
