package main.java.dao;

import main.java.models.Seat;
import main.java.models.Session;
import java.io.*;

import java.util.HashMap;
import java.util.Map;

public class SessionDao implements IDao<Session> {
    private Map<String, Session> sessions = new HashMap<>();
    private final String filePath = "src/main/resources/sessions.ser";

    @Override
    public void save(Session session) {
        sessions.put(session.getSessionId(), session);
        saveData();
    }

    @Override
    public Session get(String id) {
        return sessions.get(id);
    }

    @Override
    public Map<String, Session> getAll() {
        return new HashMap<>(sessions);
    }

    @Override
    public void update(Session session) {
        sessions.put(session.getSessionId(), session);
        saveData();
    }

    @Override
    public void delete(String id) {
        sessions.remove(id);
        saveData();
    }

    @Override
    public void initializeData() {
        File file = new File(filePath);
        File dir = new File("src/main/resources");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                sessions = (Map<String, Session>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(sessions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}