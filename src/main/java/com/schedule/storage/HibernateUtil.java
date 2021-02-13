package com.schedule.storage;

import com.schedule.app.Task;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {

        try {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Task.class);
            return configuration.buildSessionFactory(new StandardServiceRegistryBuilder().build());

        } catch (Exception e) {
            e.printStackTrace();
            throw new SessionException("Error building the factory session");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
