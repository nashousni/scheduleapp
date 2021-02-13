package com.schedule.storage;

public class StorageGenerator {

    private AppStorage generateur;

    /**
     *
     * @param dbName : the database name
     *               find database name in a set of names
     */
    public StorageGenerator(String dbName) {
        generateur = StorageContainer.getInstance().getComponent(dbName);
    }

    public AppStorage getDb() {
        return generateur;
    }

}
