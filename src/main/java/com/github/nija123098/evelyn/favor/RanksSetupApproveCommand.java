package com.github.nija123098.evelyn.favor;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RanksSetupApproveCommand extends AbstractCommand {
    public RanksSetupApproveCommand() {
        super(RanksSetupCommand.class, "approve", null, null, null, "Cancels the setupranks command");
    }
    @Command
    public static void command(Guild guild, MessageMaker maker) {
        Runnable task = RanksSetupCommand.TASK_CACHE.getIfPresent(guild);
        if (task != null) {
            RanksSetupCommand.TASK_CACHE.invalidate(guild);
            task.run();
        }
        else maker.append("It seems this either timed out or was activated already");
    }
}
