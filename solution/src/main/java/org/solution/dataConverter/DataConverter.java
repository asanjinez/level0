package org.solution.dataConverter;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public interface DataConverter<T> {
    Optional<T> load(String route) throws JsonProcessingException;
    boolean save(String route,T object);
}
