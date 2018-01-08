package com.github.nija123098.evelyn.favor;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

public class RanksSetupApproveCommand extends AbstractCommand {
    public RanksSetupApproveCommand() {
        super(RanksSetupCommand.class, "approve", null, null, null, "Cancels the setupranks command");
    }
    @Command
    public void command(Guild guild, MessageMaker maker){
        Runnable task = RanksSetupCommand.TASK_MAP.remove(guild);
        if (task != null) task.run();
        else maker.append("It seems this either timed out or was activated already");
    }
}
