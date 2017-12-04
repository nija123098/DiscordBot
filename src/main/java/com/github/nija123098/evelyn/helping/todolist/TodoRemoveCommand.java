package com.github.nija123098.evelyn.helping.todolist;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TodoRemoveCommand extends AbstractCommand {
    public TodoRemoveCommand() {
        super(TodoListCommand.class, "remove", null, null, null, "Removes an item from your todo list");
    }
    @Command
    public void command(User user, @Argument(info = "The todo number") Integer item){
        ConfigHandler.alterSetting(TodoListConfig.class, user, todoItems -> todoItems.remove((int) item));
    }
}
