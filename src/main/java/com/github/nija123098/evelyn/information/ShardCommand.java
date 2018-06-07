package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Shard;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ShardCommand extends AbstractCommand {
    public ShardCommand() {
        super("shard", ModuleLevel.INFO, null, null, "Shows the current shard");
    }
    @Command
    public void command(Shard shard, @Argument(optional = true, replacement = ContextType.NONE) Long lon, MessageMaker maker) {
        maker.append("You are on shard " + (lon == null ? shard.getID() : (lon >> 22) % Shard.getCount()));
    }
}
