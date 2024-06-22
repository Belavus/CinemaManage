package main.java.dao;

import main.java.models.Hall;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock; //for synchronization

public class HallDao implements IDao<Hall> {
    private Map<Integer, Hall> halls = new HashMap<>();
    private final String filePath;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public HallDao(String filePath) {
        this.filePath = filePath;
        initializeData();
    }

    @Override
    public void save(Hall hall) {
        lock.writeLock().lock();
        try {
            halls.put(hall.getHallNumber(), hall);
            saveData();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Hall get(String id) {
        lock.readLock().lock();
        try {
            return halls.get(Integer.parseInt(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Map<String, Hall> getAll() {
        lock.readLock().lock();
        try {
            Map<String, Hall> hallMap = new HashMap<>();
            for (Map.Entry<Integer, Hall> entry : halls.entrySet()) {
                hallMap.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            return hallMap;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void update(Hall hall) {
        lock.writeLock().lock();
        try {
            halls.put(hall.getHallNumber(), hall);
            saveData();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(String id) {
        lock.writeLock().lock();
        try {
            halls.remove(Integer.parseInt(id));
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
                    halls = (Map<Integer, Hall>) ois.readObject();
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
            oos.writeObject(halls);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
}