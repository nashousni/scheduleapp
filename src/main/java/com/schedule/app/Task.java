package com.schedule.app;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Task")
public class Task {

    @Id
    @Column(name = "id")
    private final String id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "eventDate")
    private long eventDate;
    @Column(name = "beginTime")
    private long beginTime;
    @Column(name = "endTime")
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
