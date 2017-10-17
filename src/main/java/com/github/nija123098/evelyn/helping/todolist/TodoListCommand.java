package com.github.nija123098.evelyn.helping.todolist;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.service.services.ScheduleService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class TodoListCommand extends AbstractCommand {
    public TodoListCommand() {
        super("todo", ModuleLevel.HELPER, null, null, "Helps you remember things!");
    }
    @Command
    public void command(User user, MessageMaker maker, String s, Message message){
        if (s != null && !s.isEmpty()){
            CommandHandler.attemptInvocation("todo add " + s, user, message, null);
            return;
        }
        List<TodoItem> items = ConfigHandler.getSetting(TodoListConfig.class, user);
        if (items.isEmpty()) maker.append("Your todo list is empty!");
        else {
            maker.append("Your todo list!");
            AtomicInteger integer = new AtomicInteger();
            items.forEach(todoItem -> maker.getNewListPart().append(integer + ". " + todoItem.getTodo() + "time until: " + todoItem.getScheduledTime()));
        }
    }
    static void remind(long delay, User user, TodoItem todoItem){
        ScheduleService.schedule(delay, () -> new MessageMaker(user).appendAlternate(true, "You put an item on your todo list for me to remind you of.\n", todoItem.getTodo()).send());
    }

    @Override
    protected String getLocalUsages() {
        return "todo // get your todo list";
    }
}
