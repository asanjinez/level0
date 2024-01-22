package org.solution.manager;

public interface DataManager<T> {
    boolean create(T data);
    boolean delete(T data);
}