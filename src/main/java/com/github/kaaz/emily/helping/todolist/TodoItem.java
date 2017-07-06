package com.github.kaaz.emily.helping.todolist;

/**
 * Made by nija123098 on 5/17/2017.
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
