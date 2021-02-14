package com.schedule.storage;

public class StorageGenerator {

    private final AppStorage generator;

    /**
     *
     * @param dbName : the database name
     *
     */
    public StorageGenerator(String dbName) {
        generator = StorageContainer.getInstance().getComponent(dbName);
    }

    public AppStorage getDb() {
        return generator;
    }

}
