package main.java.dao;

import main.java.models.Booking;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.locks.ReentrantReadWriteLock; //for synchronization

public class BookingDao implements IDao<Booking> {
    private Map<String, Booking> bookings = new HashMap<>();
    private final String filePath;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public BookingDao(String filePath) {
        this.filePath = filePath;
        initializeData();
    }

    @Override
    public void save(Booking booking) {
        lock.writeLock().lock();
        try {
            bookings.put(booking.getBookingId(), booking);
            saveData();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Booking get(String id) {
        lock.readLock().lock();
        try {
            return bookings.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Map<String, Booking> getAll() {
        lock.readLock().lock();
        try {
            return new HashMap<>(bookings);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void update(Booking booking) {
        lock.writeLock().lock();
        try {
            bookings.put(booking.getBookingId(), booking);
            saveData();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(String id) {
        lock.writeLock().lock();
        try {
            bookings.remove(id);
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
                    bookings = (Map<String, Booking>) ois.readObject();
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
            oos.writeObject(bookings);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
}