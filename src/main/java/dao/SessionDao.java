package main.java.dao;

import main.java.models.Session;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock; //for synchronization

public class SessionDao implements IDao<Session> {
    private Map<String, Session> sessions = new HashMap<>();
    private final String filePath;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public SessionDao(String filePath) {
        this.filePath = filePath;
        initializeData();
    }

    @Override
    public void save(Session session) {
        lock.writeLock().lock();
        try {
            sessions.put(session.getSessionId(), session);
            saveData();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Session get(String id) {
        lock.readLock().lock();
        try {
            return sessions.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Map<String, Session> getAll() {
        lock.readLock().lock();
        try {
            return new HashMap<>(sessions);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void update(Session session) {
        lock.writeLock().lock();
        try {
            sessions.put(session.getSessionId(), session);
            saveData();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(String id) {
        lock.writeLock().lock();
        try {
            sessions.remove(id);
            saveData();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void initializeData() {
        lock.writeLock().lock();
        try {
            File file = new File(filePath);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                    sessions = (Map<String, Session>) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void saveData() {
        lock.writeLock().lock();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(sessions);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
