package com.github.nija123098.evelyn.helping.todolist;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.launcher.Launcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TodoListConfig extends AbstractConfig<List<TodoItem>, User> {
    public TodoListConfig() {
        super("todo_list", "", ConfigCategory.STAT_TRACKING, new ArrayList<>(0), "The list of todo items for a user");
        Launcher.registerPostStartup(() -> {
            long current = System.currentTimeMillis();
            this.getNonDefaultSettings().forEach((user, todoItems) -> todoItems.forEach(todoItem -> TodoListCommand.remind(todoItem.getScheduledTime() - current, user, todoItem)));
        });
    }
}
