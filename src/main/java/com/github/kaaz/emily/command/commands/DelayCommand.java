package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Convert;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.Time;

/**
 * Made by nija123098 on 4/29/2017.
 */
public class DelayCommand extends AbstractCommand {
    public DelayCommand() {
        super("delay", ModuleLevel.NONE, null, null, "Executes a command after a specified delay, this command does not detect exceptions in advance and my soft fail");
    }
    @Command
    public void command(@Convert Time time, String s, User user, Message message, Reaction reaction){
        ScheduleService.schedule(time.timeUntil(), () -> CommandHandler.attemptInvocation(s, user, message, reaction));
    }
}
