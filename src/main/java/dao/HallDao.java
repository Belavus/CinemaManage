package main.java.dao;

import main.java.models.Hall;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HallDao implements IDao<Hall> {
    private Map<Integer, Hall> halls = new HashMap<>();
    private final String filePath;

    public HallDao(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(Hall hall) {
        halls.put(hall.getHallNumber(), hall);
        saveData();
    }

    @Override
    public Hall get(String id) {
        return halls.get(Integer.parseInt(id));
    }

    @Override
    public Map<String, Hall> getAll() {
        Map<String, Hall> hallMap = new HashMap<>();
        for (Map.Entry<Integer, Hall> entry : halls.entrySet()) {
            hallMap.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return hallMap;
    }

    @Override
    public void update(Hall hall) {
        halls.put(hall.getHallNumber(), hall);
        saveData();
    }

    @Override
    public void delete(String id) {
        halls.remove(Integer.parseInt(id));
        saveData();
    }

    @Override
    public void initializeData() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                halls = (Map<Integer, Hall>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(halls);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}