package com.github.nija123098.evelyn.helping.todolist;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.Time;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class TodoAddCommand extends AbstractCommand {
    public TodoAddCommand() {
        super(TodoListCommand.class, "add", null, null, null, "Adds a todo item to your list");
    }
    @Command
    public static void command(User user, @Argument(optional = true, replacement = ContextType.NONE, info = "Time until reminder") Time time, @Argument(info = "The thing to remind you of") String arg){
        TodoItem todoItem = new TodoItem(time == null ? null : time.schedualed(), arg);
        if (time != null) TodoListCommand.remind(time.timeUntil(), user, todoItem);
        ConfigHandler.alterSetting(TodoListConfig.class, user, todoItems -> todoItems.add(todoItem));
    }
}
