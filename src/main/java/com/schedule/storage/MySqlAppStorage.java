package com.schedule.storage;

import com.schedule.app.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collection;
import java.util.List;

public class MySqlAppStorage implements AppStorage{
    private static MySqlAppStorage instance;

    private SessionFactory sessionFactory;

    private MySqlAppStorage() {
        init();
    }

    private void init() {
         sessionFactory = HibernateUtil.getSessionFactory();
    }


    @Override
    public void addTask(Task task) {
        Session session = beginSession();
        session.saveOrUpdate(task);
        closeSession(session);
    }

    @Override
    public void deleteTask(Task task) {
        Session session = beginSession();
        session.delete(task);
        closeSession(session);
    }

    @Override
    public void updateTask(Task task) {
        Session session = beginSession();
        session.update(task);
        closeSession(session);
    }

    @Override
    public Collection<Task> getTasks() {
        Session session = beginSession();
        List<Task> selectedTasks = session.createQuery("SELECT a FROM Task a", Task.class).getResultList();
        closeSession(session);
        return selectedTasks;
    }

    private Session beginSession() {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        return session;
    }

    private void closeSession(Session session) {
        session.getTransaction().commit();
        session.close();
    }

    public void saveTasks(List<Task> tasks) {
        Session session = beginSession();
        tasks.forEach(session::saveOrUpdate);
        closeSession(session);
    }

    public static MySqlAppStorage getInstance() {
        if(instance == null) {
            instance = new MySqlAppStorage();
        }
        return instance;
    }
}
