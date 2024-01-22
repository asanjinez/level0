package org.solution.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.solution.exceptions.CustomExceptions.*;

import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class JsonConverter<T> implements Converter<T> {
    private final TypeReference<T> typeReference;

    public JsonConverter(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public Optional<T> load(String route) throws FileLoadException {
        try {
            String serializedObject = new String(Files.readAllBytes(Paths.get(route)));
            ObjectMapper mapper = new ObjectMapper();
            T readObject = mapper.readValue(serializedObject, typeReference);
            return Optional.of(readObject);
        } catch (IOException io) {
            throw new FileLoadException("Error loading file", io);
        }
    }

    @Override
    public boolean save(String route, T object) throws FileSaveException {
        try (FileWriter fileWriter = new FileWriter(route, false)) {
            ObjectMapper mapper = new ObjectMapper();
            String string = mapper.writeValueAsString(object);
            fileWriter.write(string);
            fileWriter.flush();
            return true;
        } catch (IOException io) {
            throw new FileSaveException("Error writing file", io);
        }
    }
}