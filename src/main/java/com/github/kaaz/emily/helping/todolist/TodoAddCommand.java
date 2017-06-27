package com.github.kaaz.emily.helping.todolist;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.Time;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class TodoAddCommand extends AbstractCommand {
    public TodoAddCommand() {
        super(TodoListCommand.class, "add", null, null, null, "Adds a todo item to your list");
    }
    @Command
    public void command(User user, MessageMaker maker, @Argument(optional = true, info = "The delay") Time time, @Argument(info = "The thing to remind you of") String arg){
        TodoItem todoItem = new TodoItem(time == null ? null : time.schedualed(), arg);
        if (time != null) TodoListCommand.remind(time.timeUntil(), user, todoItem);
        ConfigHandler.alterSetting(TodoListConfig.class, user, todoItems -> todoItems.add(todoItem));
    }
}
