package com.github.nija123098.evelyn.moderation.gameroles;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GameRolesCommand extends AbstractCommand {
    public GameRolesCommand() {
        super("gameroles", ModuleLevel.ADMINISTRATIVE, null, null, "Manages gameroles for automatic adding a role when a user starts playing a game");
    }
    @Command
    public void command(MessageMaker maker){
        maker.append("Ping nija to collect your prize if you see this");
    }
}
