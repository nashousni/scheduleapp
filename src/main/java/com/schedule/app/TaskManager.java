package com.schedule.app;

import com.schedule.storage.AppStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskManager {

    private  AppStorage storage;

    private List<Task> tasks;

    private static TaskManager taskManager = null;

    private TaskManager(AppStorage storage) {
        this.storage = storage;
        tasks = new ArrayList<>();
    }

    public boolean saveTask(Task task) {
        boolean taskAlreadyExists = tasks.stream().anyMatch(task1 -> task1.equals(task));
        if (taskAlreadyExists) {
            throw new TaskManagementException(String.format("Task '%s' already exists at this date", task.getName()));
        }
        return tasks.add(task);
    }

    public void addTask(Task task) {
        storage.addTask(task);
        saveTask(task);
    }

    public void deleteTask(Task task) {
        storage.deleteTask(task);
        tasks.remove(task);
    }

    public void updateTask(Task task) {
        storage.updateTask(task);
        Task task1 = findTask(task.getId());
        if (task1 != null) {
            task1.setEventDate(task.getEventDate());
        }
    }

    public Task findTask(String taskId) {
        return tasks.stream().filter(task -> task.getId().equals(taskId)).findFirst().orElse(null);
    }

    public List<Task> findTasks (String taskName) {
        return tasks.stream().filter(task -> task.getName().equals(taskName)).collect(Collectors.toList());
    }

    public Optional<Task> findOptionalTask(String taskId) {
        return tasks.stream().filter(task -> task.getName().equals(taskId)).findFirst();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public static TaskManager getInstance(AppStorage storage) {
        if (taskManager == null) {
            taskManager = new TaskManager(storage);
        }
        return taskManager;
    }


}