package com.schedule.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskManager {

    private List<Task> tasks;

    private static TaskManager taskManager = null;

    private TaskManager() {
        tasks = new ArrayList<>();
    }

    public boolean addTask(Task task) {
        return tasks.add(task);
    }

    public boolean deleteTask(Task task) {
        return tasks.remove(task);
    }

    public boolean updateTask(Task task) {
        Task task1 = findTask(task.getName());
        if (task1 != null) {
            task1.setEventDate(task.getEventDate());
            return true;
        }
        return false;
    }

    public Task findTask(String taskId) {
        return tasks.stream().filter(task -> task.getName().equals(taskId)).findFirst().orElse(null);
    }

    public Optional<Task> findOptionalTask(String taskId) {
        return tasks.stream().filter(task -> task.getName().equals(taskId)).findFirst();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public static TaskManager getInstance() {
        if (taskManager == null) {
            taskManager = new TaskManager();
        }
        return taskManager;
    }


}