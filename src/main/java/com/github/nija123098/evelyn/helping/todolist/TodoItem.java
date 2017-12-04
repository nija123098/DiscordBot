package com.github.nija123098.evelyn.helping.todolist;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TodoItem {
    private Long scheduledTime;
    private String todo;
    protected TodoItem() {}
    TodoItem(Long scheduledTime, String todo) {
        this.scheduledTime = scheduledTime;
        this.todo = todo;
    }
    public Long getScheduledTime() {
        return this.scheduledTime;
    }
    public String getTodo() {
        return this.todo;
    }
}
