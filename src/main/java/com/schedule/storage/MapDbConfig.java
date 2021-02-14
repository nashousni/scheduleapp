package com.schedule.storage;

public class MapDbConfig {
    private String db_type;
    private String drive_name;
    private String db_file;

    public String getDb_type() {
        return db_type;
    }

    public void setDb_type(String db_type) {
        this.db_type = db_type;
    }

    public String getDrive_name() {
        return drive_name;
    }

    public void setDrive_name(String drive_name) {
        this.drive_name = drive_name;
    }

    public String getDb_file() {
        return db_file;
    }

    public void setDb_file(String db_file) {
        this.db_file = db_file;
    }
}
