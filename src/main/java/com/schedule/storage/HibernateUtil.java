package com.schedule.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.schedule.app.Task;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.Properties;

public final class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    static final String DIALECT = "hibernate.dialect";
    static final String SHOW_SQL = "hibernate.show_sql";

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {

        try {
            /*
            Configuration
           */
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Task.class); // add class to be mapped

            Properties properties = new Properties();
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.findAndRegisterModules();// use to register Date properly (not necessary here)
            String path = System.getProperty("user.home") + "/scheduleappconfig.yaml";
            HibernateConfig config = mapper.readValue(new File(path), HibernateConfig.class);
            Connection connection = config.getConnection();

            properties.put(ConnectionParameters.USERNAME, connection.getUsername());
            properties.put(ConnectionParameters.PASSWORD, connection.getPassword());
            properties.put(ConnectionParameters.URL, connection.getUrl());
            properties.put(ConnectionParameters.DRIVER_CLASS, connection.getDriver_class());
            properties.put(DIALECT, config.getDialect());
            properties.put(SHOW_SQL, config.isShow_sql());
            configuration.setProperties(properties);
            return configuration
                    .buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new SessionException("Error building the factory session");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static class ConnectionParameters {
        static final String USERNAME = "hibernate.connection.username";
        static final String PASSWORD = "hibernate.connection.password";
        static final String URL = "hibernate.connection.url";
        static final String DRIVER_CLASS = "hibernate.connection.driver_class";
    }
}
