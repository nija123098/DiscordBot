package com.github.nija123098.evelyn.helping.todolist;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class TodoListConfig extends AbstractConfig<List<TodoItem>, User> {
    public TodoListConfig() {
        super("todo_list", ConfigCategory.STAT_TRACKING, new ArrayList<>(0), "The list of todo items for a user");
        Launcher.registerAsyncStartup(() -> {
            long current = System.currentTimeMillis();
            this.getNonDefaultSettings().forEach((user, todoItems) -> todoItems.forEach(todoItem -> TodoListCommand.remind(todoItem.getScheduledTime() - current, user, todoItem)));
        });
    }
}
