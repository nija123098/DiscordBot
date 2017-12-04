package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.Time;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DelayCommand extends AbstractCommand {
    public DelayCommand() {
        super("delay", ModuleLevel.NONE, null, null, "Executes a command after a specified delay, this command does not detect exceptions in advance and my soft fail");
    }
    @Command
    public void command(@Argument Time time, String s, User user, Message message, Reaction reaction){
        ScheduleService.schedule(time.timeUntil(), () -> CommandHandler.attemptInvocation(s, user, message, reaction));
    }
}
