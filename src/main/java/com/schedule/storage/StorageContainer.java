package com.schedule.storage;

import java.util.HashMap;
import java.util.Map;

public class StorageContainer {
    private static  StorageContainer instance ;

    private final Map<String, AppStorage> map;
    public StorageContainer(){
        map = new HashMap<>();
        map.put("mapdb", MapDbStorage.getInstance());
        map.put("mysql", MySqlAppStorage.getInstance());

    }
    static StorageContainer getInstance(){
        if(instance==null)
            instance = new StorageContainer();
        return instance;
    }

    public AppStorage getComponent(String nom) {
        return map.get(nom);
    }
}