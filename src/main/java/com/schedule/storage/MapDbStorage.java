package com.schedule.storage;

import com.schedule.app.IllegalTaskException;
import com.schedule.app.Task;
import com.schedule.app.TaskSerializer;
import com.schedule.app.UuidSerializer;
import org.apache.commons.lang3.SystemUtils;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class MapDbStorage {

    private ConcurrentMap<String, Date> tasksMap2; // Date instead of LocalDate because MabDb doesn't have LocalDate.Serializer(LocalDate == Java 8)

    private ConcurrentMap<UUID, Task> tasksMap;

    private DB db;

    private static MapDbStorage instance;

    private MapDbStorage() {
        init();
    }

    public void saveTasks(List<Task> tasks) {
        tasksMap.clear();
        tasks.forEach(task -> tasksMap.put(checkNodeId(task.getId()), new Task(task.getId(), task.getName(), task.getDescription(), task.getEventDate())));
        db.commit();
    }

    public void addTask(Task task) {
        tasksMap.put(checkNodeId(task.getId()), new Task(task.getId(), task.getName(), task.getDescription(), task.getEventDate()));
        db.commit();
    }

    private void init() {
        DBMaker.Maker maker = DBMaker.fileDB("file.db").transactionEnable().closeOnJvmShutdown();

        // it is not recommended to use memory map (mmap) files  on Windows (crash)
        // http://www.mapdb.org/blog/mmap_files_alloc_and_jvm_crash/
        if (!SystemUtils.IS_OS_WINDOWS) {
            maker.fileMmapEnableIfSupported().fileMmapPreclearDisable();
        }
        db = maker.make();

        tasksMap = db.hashMap("tasks", UuidSerializer.INSTANCE, TaskSerializer.INSTANCE).createOrOpen();
    }


    public Map<UUID, Task> getTasksMap() {
        return new HashMap(tasksMap);
    }

    public static MapDbStorage getInstance() {
        if(instance == null) {
            instance = new MapDbStorage();
        }
        return instance;
    }

    public static UUID checkNodeId(String taskId) {
        try {
            return UUID.fromString(taskId);
        } catch (IllegalArgumentException e) {
            throw new IllegalTaskException("Task id '" + taskId + "' is expected to be a UUID");
        }
    }

}
