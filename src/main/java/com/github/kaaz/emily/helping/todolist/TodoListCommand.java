package com.github.kaaz.emily.helping.todolist;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.service.services.ScheduleService;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class TodoListCommand extends AbstractCommand {
    public TodoListCommand() {
        super("todo", ModuleLevel.HELPER, null, null, "");
    }
    @Command
    public void command(User user, MessageMaker maker){
        maker.append("Your todo list!");
        AtomicInteger integer = new AtomicInteger();
        ConfigHandler.getSetting(TodoListConfig.class, user).forEach(todoItem -> {
            maker.getNewListPart().append(integer + ". " + todoItem.getTodo());
        });
    }
    static void remind(long delay, User user, TodoItem todoItem){
        ScheduleService.schedule(delay, () -> new MessageMaker(user).appendAlternate(true, "You put a item on your todo list for me to remind you of.\n", todoItem.getTodo()).send());
    }
}
