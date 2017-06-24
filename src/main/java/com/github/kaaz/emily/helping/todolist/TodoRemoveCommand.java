package com.github.kaaz.emily.helping.todolist;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class TodoRemoveCommand extends AbstractCommand {
    public TodoRemoveCommand() {
        super(TodoListCommand.class, "remove", null, null, null, "Removes an item from your todo list");
    }
    @Command
    public void command(User user, MessageMaker maker, @Argument(info = "The todo number") Integer item){
        ConfigHandler.alterSetting(TodoListConfig.class, user, todoItems -> todoItems.remove((int) item));
    }
}
