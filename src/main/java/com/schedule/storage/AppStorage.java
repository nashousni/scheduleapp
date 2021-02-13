package com.schedule.storage;

import com.schedule.app.Task;

import java.util.Collection;

public interface AppStorage {

    void addTask(Task task);

    void deleteTask(Task task);

    void updateTask(Task task);

    Collection<Task> getTasks();

}
