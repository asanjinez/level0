package org.solution;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;



public class Authenticator {
    Map<String,String> users;

    public Authenticator() {
        this. users = this.loadUsers();
    }

    public boolean registerUser(String username, String password){
        boolean validRegister = false;
        users.put(username,password);

        try {
            JSONObject jsonObject = new JSONObject(users);
            FileWriter fileWriter = new FileWriter("src\\main\\resources\\users\\users.json",false);
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();

            System.out.println("User successfully  created");
            validRegister = true;

        } catch (IOException io){
            users.remove(username);
            System.out.println("Error creating user");
            System.out.println(io.getMessage());
        }

        return validRegister;

    }

    public boolean validUser(String username, String password){
        if (this.users.containsKey(username) && users.get(username).equals(password) ){
            System.out.println("Logged in");
            return true;
        }
        System.out.println("Invalid username or password");
        System.out.println("_________________");
        return false;
    }

    public boolean validUsername(String username){
        if(!users.containsKey(username))
            return true;

        System.out.println("This username alredy exists, try to log in");
        System.out.println("_________________");
        return false;
    }



    private static Map<String, String> loadUsers() {
        Map<String, String> map;
        try {
            String users = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\users\\users.json")));
            JSONObject jsonObject = new JSONObject(users);
            map = toMap(jsonObject);

            System.out.println(map.size() + " Users loaded");
            System.out.println("_________________");

        } catch (IOException io){
            System.out.println("Users File not found");
            System.out.println(io.getMessage());
            map = new HashMap<>();
        }

        return map;
    }

    // Convert from Json to Map
    private static Map<String, String> toMap(JSONObject jsonObject) {
        Map<String, String> map = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.getString(key));
        }
        return map;
    }
}
