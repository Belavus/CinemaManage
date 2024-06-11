package main.java.dao;

import main.java.models.Booking;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BookingDao implements IDao<Booking> {
    private Map<String, Booking> bookings = new HashMap<>();
    private final String filePath;

    public BookingDao(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
        saveData();
    }

    @Override
    public Booking get(String id) {
        return bookings.get(id);
    }

    @Override
    public Map<String, Booking> getAll() {
        return new HashMap<>(bookings);
    }

    @Override
    public void update(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
        saveData();
    }

    @Override
    public void delete(String id) {
        bookings.remove(id);
        saveData();
    }

    @Override
    public void initializeData() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                bookings = (Map<String, Booking>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}