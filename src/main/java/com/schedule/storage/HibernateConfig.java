package com.schedule.storage;

public class HibernateConfig {
    private String db_type;
    private Connection connection;
    private String dialect;
    private boolean show_sql;

    public String getDb_type() {
        return db_type;
    }

    public void setDb_type(String db_type) {
        this.db_type = db_type;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public boolean isShow_sql() {
        return show_sql;
    }

    public void setShow_sql(boolean show_sql) {
        this.show_sql = show_sql;
    }
}
