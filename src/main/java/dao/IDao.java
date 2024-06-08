package main.java.dao;

import java.util.Map;

public interface IDao<T> {
    void save(T t);
    T get(String id);
    Map<String, T> getAll();
    void update(T t);
    void delete(String id);
    void initializeData();
    void saveData();
}

