package com.schedule.app;

import com.schedule.storage.MapDbStorage;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

import java.io.IOException;
import java.io.Serializable;

public class TaskSerializer implements Serializer<Task>, Serializable {

    public static final TaskSerializer INSTANCE = new TaskSerializer();

    @Override
    public void serialize(@NotNull DataOutput2 out, @NotNull Task task) throws IOException {
        UuidSerializer.INSTANCE.serialize(out, MapDbStorage.checkNodeId(task.getId()));
        out.writeUTF(task.getName());
        out.writeUTF(task.getDescription() == null ? "" : task.getDescription());
        out.writeLong(task.getEventDate());
    }

    @Override
    public Task deserialize(@NotNull DataInput2 input, int i) throws IOException {
        String taskId = UuidSerializer.INSTANCE.deserialize(input, i).toString();
        String name = input.readUTF();
        String description = input.readUTF();
        long eventDate = input.readLong();
        return new Task(taskId, name, description, eventDate);
    }
}
