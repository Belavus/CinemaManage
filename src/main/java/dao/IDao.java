package main.java.dao;

import java.util.List;

public interface IDao<T> {
    void save(T t);
    T get(String id);
    List<T> getAll();
    void update(T t);
    void delete(String id);
    void initializeData();
    void saveData();
}

