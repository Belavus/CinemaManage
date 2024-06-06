package main.java.dao;

import main.java.models.Booking;
import main.java.models.Seat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.*;

public class BookingDao implements IDao<Booking> {
    private List<Booking> bookings = new ArrayList<>();
    private final String filePath = "bookings.txt";

    @Override
    public void save(Booking booking) {
        bookings.add(booking);
        saveData();
    }

    @Override
    public Booking get(String id) {
        Optional<Booking> booking = bookings.stream().filter(b -> b.getBookingId().equals(id)).findFirst();
        return booking.orElse(null);
    }

    @Override
    public List<Booking> getAll() {
        return new ArrayList<>(bookings);
    }

    @Override
    public void update(Booking booking) {
        delete(booking.getBookingId());
        save(booking);
    }

    @Override
    public void delete(String id) {
        bookings.removeIf(booking -> booking.getBookingId().equals(id));
        saveData();
    }

    @Override
    public void initializeData() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String bookingId = parts[0];
                String sessionId = parts[1];
                int row = Integer.parseInt(parts[2]);
                int column = Integer.parseInt(parts[3]);
                boolean isBooked = Boolean.parseBoolean(parts[4]);
                Seat seat = new Seat(row, column);
                seat.setBooked(isBooked);
                bookings.add(new Booking(bookingId, sessionId, seat));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Booking booking : bookings) {
                writer.write(booking.getBookingId() + "," + booking.getSessionId() + "," + booking.getSeat().getRow() +
                        "," + booking.getSeat().getColumn() + "," + booking.getSeat().isBooked());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}