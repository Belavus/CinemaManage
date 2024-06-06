package main.java.dao;

import main.java.models.Seat;
import main.java.models.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.*;

public class SessionDao implements IDao<Session> {
    private List<Session> sessions = new ArrayList<>();
    private final String filePath = "sessions.txt";

    @Override
    public void save(Session session) {
        sessions.add(session);
        saveData();
    }

    @Override
    public Session get(String id) {
        Optional<Session> session = sessions.stream().filter(s -> s.getSessionId().equals(id)).findFirst();
        return session.orElse(null);
    }

    @Override
    public List<Session> getAll() {
        return new ArrayList<>(sessions);
    }

    @Override
    public void update(Session session) {
        delete(session.getSessionId());
        save(session);
    }

    @Override
    public void delete(String id) {
        sessions.removeIf(session -> session.getSessionId().equals(id));
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
                String sessionId = parts[0];
                String movieName = parts[1];
                String time = parts[2];
                List<Seat> seats = new ArrayList<>();
                for (int i = 3; i < parts.length; i += 3) {
                    int row = Integer.parseInt(parts[i]);
                    int column = Integer.parseInt(parts[i + 1]);
                    boolean isBooked = Boolean.parseBoolean(parts[i + 2]);
                    seats.add(new Seat(row, column));
                    seats.get(seats.size() - 1).setBooked(isBooked);
                }
                sessions.add(new Session(sessionId, movieName, time, seats));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Session session : sessions) {
                writer.write(session.getSessionId() + "," + session.getMovieName() + "," + session.getTime());
                for (Seat seat : session.getSeats()) {
                    writer.write("," + seat.getRow() + "," + seat.getColumn() + "," + seat.isBooked());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}