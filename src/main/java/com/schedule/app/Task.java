package com.schedule.app;

import java.util.Objects;

public class Task {

    private final String id;

    private String name;

    private String description;

    private long eventDate;

    public Task(String id, String name, long date) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.eventDate = date;
    }

    public Task(String id, String name, String description,  long date) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description =description;
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

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                '}';
    }

}
