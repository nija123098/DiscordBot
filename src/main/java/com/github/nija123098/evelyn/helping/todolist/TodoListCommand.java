package com.github.nija123098.evelyn.helping.todolist;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.ThreadHelper;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TodoListCommand extends AbstractCommand {
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Todo-List-Thread"));
    public TodoListCommand() {
        super("todo", ModuleLevel.HELPER, null, null, "Helps you remember things!");
    }
    @Command
    public void command(User user, MessageMaker maker, String s, Message message) {
        if (s != null && !s.isEmpty()) {
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
    static void remind(long delay, User user, TodoItem todoItem) {
        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> {
            new MessageMaker(user).mustEmbed().getTitle().appendRaw("What todo").getMaker().getNewFieldPart().withBoth("\u200b", "\nHere's what you told me to remind you of:\n" + todoItem.getTodo()).getMessageProducer().send();
            ConfigHandler.alterSetting(TodoListConfig.class, user, todoItems -> todoItems.remove(todoItem));
        }, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    protected String getLocalUsages() {
        return "#  todo // get your todo list";
    }
}
