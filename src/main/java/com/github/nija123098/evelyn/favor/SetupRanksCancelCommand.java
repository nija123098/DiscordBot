package com.github.nija123098.evelyn.favor;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.service.services.ScheduleService;

public class SetupRanksCancelCommand extends AbstractCommand {
    public SetupRanksCancelCommand() {
        super(SetupRanksCommand.class, "cancel", null, null, null, "Cancels the setupranks command");
    }
    @Command
    public void command(Guild guild, MessageMaker maker){
        ScheduleService.ScheduledTask task = SetupRanksCommand.SCHEDULED_TASK_MAP.remove(guild);
        if (task != null) task.cancel();
        else maker.append("Oh no, it seems you were too late, that's too bad.");
    }
}
