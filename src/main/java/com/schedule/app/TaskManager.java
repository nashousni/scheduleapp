package com.schedule.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskManager {

    private List<Task> tasks;

    private static TaskManager taskManager = null;

    private TaskManager() {
        tasks = new ArrayList<>();
    }

    public boolean addTask(Task task) {
        boolean taskAlreadyExists = tasks.stream().anyMatch(task1 -> task1.equals(task));
        if (taskAlreadyExists) {
            throw new TaskManagementException(String.format("Task '%s' already exists at this date", task.getName()));
        }
        return tasks.add(task);
    }

    public boolean deleteTask(Task task) {
        return tasks.remove(task);
    }

    public boolean updateTask(Task task) {
        Task task1 = findTask(task.getId());
        if (task1 != null) {
            task1.setEventDate(task.getEventDate());
            return true;
        }
        return false;
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

    public static TaskManager getInstance() {
        if (taskManager == null) {
            taskManager = new TaskManager();
        }
        return taskManager;
    }


}