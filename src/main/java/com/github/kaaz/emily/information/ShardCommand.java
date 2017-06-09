package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Shard;

/**
 * Made by nija123098 on 6/7/2017.
 */
public class ShardCommand extends AbstractCommand {
    public ShardCommand() {
        super("shard", ModuleLevel.INFO, null, null, "Shows the current shard");
    }
    @Command
    public void command(Shard shard, @Argument(optional = true, replacement = ContextType.NONE) Long lon, MessageMaker maker){
        maker.append("You are on shard " + (lon == null ? shard.getID() : (lon >> 22) % Shard.getCount()));
    }
}
