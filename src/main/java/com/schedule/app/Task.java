package com.schedule.app;

import java.util.Objects;

public class Task {

    private final String id;

    private String name;

    private String description;

    private long eventDate;

    private long beginTime;

    private long endTime;

    public Task(String id, String name, long date) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.eventDate = date;
    }

    public Task(String id, String name, String description, long date) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.eventDate = date;
    }

    public String getId() {
        return id;
    }

    public long getEventDate() {
        return eventDate;
    }

    public void setEventDate(long date) {
        this.eventDate = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, eventDate);
    }

    @Override
    public boolean equals(Object value) {
        if (value instanceof Task) {
            Task task = (Task) value;
            return task.getName().equals(name) && task.getEventDate() == eventDate;
        }
        return false;
    }
}
