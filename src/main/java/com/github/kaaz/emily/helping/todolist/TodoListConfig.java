package com.github.kaaz.emily.helping.todolist;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.launcher.Launcher;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.ThreadProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class TodoListConfig extends AbstractConfig<List<TodoItem>, User> {
    public TodoListConfig() {
        super("todo_list", BotRole.USER, new ArrayList<>(0), "The list of todo items for a user");
        Launcher.registerAsyncStartup(() -> {
            long current = System.currentTimeMillis();
            this.getNonDefaultSettings().forEach((user, todoItems) -> todoItems.forEach(todoItem -> TodoListCommand.remind(todoItem.getScheduledTime() - current, user, todoItem)));
        });
    }
}
