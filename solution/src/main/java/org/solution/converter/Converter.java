package org.solution.converter;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public interface Converter<T> {
    Optional<T> load(String route) throws JsonProcessingException;
    boolean save(String route,T object);
}
