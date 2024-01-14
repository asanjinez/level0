package org.solution;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JsonParser<T, M> {
    public Map<T, M> loadJson(String route) {
        Map< T, M> map = null;
        try {
            String serializedObject = new String(Files.readAllBytes(Paths.get(route)));
            JSONObject jsonObject = new JSONObject(serializedObject);
            map = toMap(jsonObject);

            System.out.println(map.size() + " Objects loaded");
            System.out.println("_________________");

        } catch (IOException io){
            System.out.println("Users File not found");
            System.out.println(io.getMessage());
        }

        return map;
    }
    public boolean writeJson(JSONObject jsonObject, String route) {
        try (FileWriter fileWriter = new FileWriter(route, false)) {
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();

//            System.out.println("Written file");
            return true;
        } catch (IOException io) {
            System.out.println("Error writting file");
            System.out.println(io.getMessage());
        }
        return false;
    }

    // Convert from Json to Map
    private Map<T, M> toMap(JSONObject jsonObject) {
        Map<T, M> map = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            map.put((T) key, (M) value);
        }
        return map;
    }
}
