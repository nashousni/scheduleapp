package com.schedule.storage;

import com.schedule.app.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class MySqlAppStorage {
    private static MySqlAppStorage instance;

    private SessionFactory sessionFactory;

    private MySqlAppStorage() {
        init();
    }

    private void init() {
         sessionFactory = HibernateUtil.getSessionFactory();
    }


    public void saveTasks(List<Task> tasks) {
        System.out.println(tasks);
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        tasks.forEach(session::saveOrUpdate);
        session.getTransaction().commit();

        session.close();

    }

    public static MySqlAppStorage getInstance() {
        if(instance == null) {
            instance = new MySqlAppStorage();
        }
        return instance;
    }
}
